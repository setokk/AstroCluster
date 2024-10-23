# -*- coding: utf-8 -*-
import torch
import torch.nn as nn
from transformers import RobertaTokenizer, RobertaModel, RobertaConfig

class UniXcoder(nn.Module):
    def __init__(self, model_name):
        """
            Build UniXcoder.

            Parameters:

            * `model_name`- huggingface model card name. e.g. microsoft/unixcoder-base
        """
        super(UniXcoder, self).__init__()
        self.tokenizer = RobertaTokenizer.from_pretrained(model_name)
        self.config = RobertaConfig.from_pretrained(model_name)
        self.config.is_decoder = True
        self.model = RobertaModel.from_pretrained(model_name, config=self.config)

        self.register_buffer("bias", torch.tril(torch.ones((1024, 1024), dtype=torch.uint8)).view(1,1024, 1024))
        self.lm_head = nn.Linear(self.config.hidden_size, self.config.vocab_size, bias=False)
        self.lm_head.weight = self.model.embeddings.word_embeddings.weight
        self.lsm = nn.LogSoftmax(dim=-1)

        self.tokenizer.add_tokens(["<mask0>"],special_tokens=True)

    def tokenize(self, inputs, mode="<encoder-only>", max_length=512, padding=False):
        """
        Convert string to token ids

        Parameters:

        * `inputs`- list of input strings.
        * `max_length`- The maximum total source sequence length after tokenization.
        * `padding`- whether to pad source sequence length to max_length.
        * `mode`- which mode the sequence will use. i.e. <encoder-only>, <decoder-only>, <encoder-decoder>
        """
        assert mode in ["<encoder-only>", "<decoder-only>", "<encoder-decoder>"]
        assert max_length < 1024

        tokenizer = self.tokenizer

        tokens_ids = []
        for x in inputs:
            tokens = tokenizer.tokenize(x)
            if mode == "<encoder-only>":
                tokens = tokens[:max_length-4]
                tokens = [tokenizer.cls_token,mode,tokenizer.sep_token] + tokens + [tokenizer.sep_token]
            elif mode == "<decoder-only>":
                tokens = tokens[-(max_length-3):]
                tokens = [tokenizer.cls_token,mode,tokenizer.sep_token] + tokens
            else:
                tokens = tokens[:max_length-5]
                tokens = [tokenizer.cls_token,mode,tokenizer.sep_token] + tokens + [tokenizer.sep_token]

            tokens_id = tokenizer.convert_tokens_to_ids(tokens)
            if padding:
                tokens_id = tokens_id + [self.config.pad_token_id] * (max_length-len(tokens_id))
            tokens_ids.append(tokens_id)
        return tokens_ids

    def decode(self, source_ids):
        """ Convert token ids to string """
        predictions = []
        for x in source_ids:
            prediction = []
            for y in x:
                t = y.cpu().numpy()
                t = list(t)
                if 0 in t:
                    t = t[:t.index(0)]
                text = self.tokenizer.decode(t,clean_up_tokenization_spaces=False)
                prediction.append(text)
            predictions.append(prediction)
        return predictions

    def forward(self, source_ids):
        """ Obtain token embeddings and sentence embeddings """
        mask = source_ids.ne(self.config.pad_token_id)
        token_embeddings = self.model(source_ids,attention_mask = mask.unsqueeze(1) * mask.unsqueeze(2))[0]
        sentence_embeddings = (token_embeddings * mask.unsqueeze(-1)).sum(1) / mask.sum(-1).unsqueeze(-1)
        return token_embeddings, sentence_embeddings

    def generate(self, source_ids, decoder_only = True, eos_id = None, beam_size = 5, max_length = 64):
        """ Generate sequence given context (source_ids) """

        # Set encoder mask attention matrix: bidirectional for <encoder-decoder>, unirectional for <decoder-only>
        if decoder_only:
            mask = self.bias[:,:source_ids.size(-1),:source_ids.size(-1)]
        else:
            mask = source_ids.ne(self.config.pad_token_id)
            mask = mask.unsqueeze(1) * mask.unsqueeze(2)

        if eos_id is None:
            eos_id = self.config.eos_token_id

        device = source_ids.device

        # Decoding using beam search
        preds = []
        zero = torch.LongTensor(1).fill_(0).to(device)
        source_len = list(source_ids.ne(1).sum(-1).cpu().numpy())
        length = source_ids.size(-1)
        encoder_output = self.model(source_ids,attention_mask=mask)
        for i in range(source_ids.shape[0]):
            context = [[x[i:i+1,:,:source_len[i]].repeat(beam_size,1,1,1) for x in y]
                     for y in encoder_output.past_key_values]
            beam = Beam(beam_size,eos_id,device)
            input_ids = beam.getCurrentState().clone()
            context_ids = source_ids[i:i+1,:source_len[i]].repeat(beam_size,1)
            out = encoder_output.last_hidden_state[i:i+1,:source_len[i]].repeat(beam_size,1,1)
            for _ in range(max_length):
                if beam.done():
                    break
                if _ == 0:
                    hidden_states = out[:,-1,:]
                    out = self.lsm(self.lm_head(hidden_states)).data
                    beam.advance(out)
                    input_ids.data.copy_(input_ids.data.index_select(0, beam.getCurrentOrigin()))
                    input_ids = beam.getCurrentState().clone()
                else:
                    length = context_ids.size(-1)+input_ids.size(-1)
                    out = self.model(input_ids,attention_mask=self.bias[:,context_ids.size(-1):length,:length],
                                       past_key_values=context).last_hidden_state
                    hidden_states = out[:,-1,:]
                    out = self.lsm(self.lm_head(hidden_states)).data
                    beam.advance(out)
                    input_ids.data.copy_(input_ids.data.index_select(0, beam.getCurrentOrigin()))
                    input_ids = torch.cat((input_ids,beam.getCurrentState().clone()),-1)
            hyp = beam.getHyp(beam.getFinal())
            pred = beam.buildTargetTokens(hyp)[:beam_size]
            pred = [torch.cat([x.view(-1) for x in p]+[zero]*(max_length-len(p))).view(1,-1) for p in pred]
            preds.append(torch.cat(pred,0).unsqueeze(0))

        preds = torch.cat(preds,0)

        return preds



class Beam(object):
    def __init__(self, size, eos, device):
        self.size = size
        self.device = device
        # The score for each translation on the beam.
        self.scores = torch.FloatTensor(size).zero_().to(device)
        # The backpointers at each time-step.
        self.prevKs = []
        # The outputs at each time-step.
        self.nextYs = [torch.LongTensor(size).fill_(0).to(device)]
        # Has EOS topped the beam yet.
        self._eos = eos
        self.eosTop = False
        # Time and k pair for finished.
        self.finished = []

    def getCurrentState(self):
        "Get the outputs for the current timestep."
        batch = self.nextYs[-1].view(-1, 1)
        return batch

    def getCurrentOrigin(self):
        "Get the backpointers for the current timestep."
        return self.prevKs[-1]

    def advance(self, wordLk):
        """
        Given prob over words for every last beam `wordLk` and attention
        `attnOut`: Compute and update the beam search.

        Parameters:

        * `wordLk`- probs of advancing from the last step (K x words)
        * `attnOut`- attention at the last step

        Returns: True if beam search is complete.
        """
        numWords = wordLk.size(1)

        # Sum the previous scores.
        if len(self.prevKs) > 0:
            beamLk = wordLk + self.scores.unsqueeze(1).expand_as(wordLk)

            # Don't let EOS have children.
            for i in range(self.nextYs[-1].size(0)):
                if self.nextYs[-1][i] == self._eos:
                    beamLk[i] = -1e20
        else:
            beamLk = wordLk[0]
        flatBeamLk = beamLk.view(-1)
        bestScores, bestScoresId = flatBeamLk.topk(self.size, 0, True, True)

        self.scores = bestScores

        # bestScoresId is flattened beam x word array, so calculate which
        # word and beam each score came from
        prevK = torch.div(bestScoresId, numWords, rounding_mode="floor")
        self.prevKs.append(prevK)
        self.nextYs.append((bestScoresId - prevK * numWords))


        for i in range(self.nextYs[-1].size(0)):
            if self.nextYs[-1][i] == self._eos:
                s = self.scores[i]
                self.finished.append((s, len(self.nextYs) - 1, i))

        # End condition is when top-of-beam is EOS and no global score.
        if self.nextYs[-1][0] == self._eos:
            self.eosTop = True

    def done(self):
        return self.eosTop and len(self.finished) >= self.size

    def getFinal(self):
        if len(self.finished) == 0:
            self.finished.append((self.scores[0], len(self.nextYs) - 1, 0))
        self.finished.sort(key=lambda a: -a[0])
        if len(self.finished) != self.size:
            unfinished=[]
            for i in range(self.nextYs[-1].size(0)):
                if self.nextYs[-1][i] != self._eos:
                    s = self.scores[i]
                    unfinished.append((s, len(self.nextYs) - 1, i))
            unfinished.sort(key=lambda a: -a[0])
            self.finished+=unfinished[:self.size-len(self.finished)]
        return self.finished[:self.size]

    def getHyp(self, beam_res):
        """
        Walk back to construct the full hypothesis.
        """
        hyps=[]
        for _,timestep, k in beam_res:
            hyp = []
            for j in range(len(self.prevKs[:timestep]) - 1, -1, -1):
                hyp.append(self.nextYs[j+1][k])
                k = self.prevKs[j][k]
            hyps.append(hyp[::-1])
        return hyps

    def buildTargetTokens(self, preds):
        sentence=[]
        for pred in preds:
            tokens = []
            for tok in pred:
                if tok==self._eos:
                    break
                tokens.append(tok)
            sentence.append(tokens)
        return sentence

import re
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader
from transformers import AdamW

# Modify the existing UniXcoder to return just sentence embeddings
class UniXcoderForEmbedding(UniXcoder):
    def forward(self, source_ids):
        """ Only return sentence embeddings """
        mask = source_ids.ne(self.config.pad_token_id)
        token_embeddings = self.model(source_ids, attention_mask=mask.unsqueeze(1) * mask.unsqueeze(2))[0]
        sentence_embeddings = (token_embeddings * mask.unsqueeze(-1)).sum(1) / mask.sum(-1).unsqueeze(-1)
        return sentence_embeddings

class TripletLoss(nn.Module):
    def __init__(self, margin=1.0):
        super(TripletLoss, self).__init__()
        self.margin = margin
        self.loss_fn = nn.TripletMarginLoss(margin=margin)

    def forward(self, anchor, positive, negative):
        return self.loss_fn(anchor, positive, negative)

class TripletDataset(Dataset):
    def __init__(self, anchor_texts, positive_texts, negative_texts, tokenizer, max_length=512):
        self.anchor_texts = anchor_texts
        self.positive_texts = positive_texts
        self.negative_texts = negative_texts
        self.tokenizer = tokenizer
        self.max_length = max_length

    def __len__(self):
        return len(self.anchor_texts)

    def __getitem__(self, idx):
        anchor = self.anchor_texts[idx]
        positive = self.positive_texts[idx]
        negative = self.negative_texts[idx]

        anchor_ids = self.tokenizer.encode(anchor, return_tensors='pt', truncation=True, max_length=self.max_length, padding='max_length')
        positive_ids = self.tokenizer.encode(positive, return_tensors='pt', truncation=True, max_length=self.max_length, padding='max_length')
        negative_ids = self.tokenizer.encode(negative, return_tensors='pt', truncation=True, max_length=self.max_length, padding='max_length')

        return anchor_ids.squeeze(0), positive_ids.squeeze(0), negative_ids.squeeze(0)


def load_datasets(anchors_path, positives_path, negatives_path, delimiter=r'\s*```[@]```\s*', regex=r'[0-9]+\s*:\s*{(.*)}'):
    files_contents = []
    paths = [anchors_path, positives_path, negatives_path]
    for path in paths:
        file_code_contents = []
        try:
            with open(path, 'r') as f:
                file_content_split = f.read().split(delimiter)
                for dataset_item in file_content_split:
                    matched = re.match(regex, dataset_item.strip(), re.DOTALL)
                    if matched:
                        code_content = matched.group(1)
                        file_code_contents.append(code_content)
        except Exception as e:
            print(f'Error reading file in path: {path}\nError: {e}\n')
            
        files_contents.append(file_code_contents)
    return files_contents[0], files_contents[1], files_contents[2]

def train_model(model, train_dataloader, optimizer, triplet_loss_fn, num_epochs=3):
    model.train()

    for epoch in range(num_epochs):
        epoch_loss = 0
        for step, batch in enumerate(train_dataloader):
            anchor_ids, positive_ids, negative_ids = batch
            anchor_ids, positive_ids, negative_ids = anchor_ids.to(device), positive_ids.to(device), negative_ids.to(device)

            anchor_embeds = model(anchor_ids)
            positive_embeds = model(positive_ids)
            negative_embeds = model(negative_ids)

            loss = triplet_loss_fn(anchor_embeds, positive_embeds, negative_embeds)
            loss.backward()
            optimizer.step()
            optimizer.zero_grad()

            epoch_loss += loss.item()

        print(f"Epoch {epoch+1}/{num_epochs} - Loss: {epoch_loss/len(train_dataloader)}")

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

model_name = "microsoft/unixcoder-base"
model = UniXcoderForEmbedding(model_name).to(device)
tokenizer = RobertaTokenizer.from_pretrained(model_name)
triplet_loss_fn = TripletLoss(margin=1.0)

anchor_contents, positive_contents, negative_contents = load_datasets('./anchors.dataset', './positives.dataset', 'negatives.dataset')
train_dataset = TripletDataset(anchor_contents, positive_contents, negative_contents, tokenizer)
train_dataloader = DataLoader(train_dataset, batch_size=8, shuffle=True)

optimizer = AdamW(model.parameters(), lr=1e-5, weight_decay=0.01)
train_model(model, train_dataloader, optimizer, triplet_loss_fn, num_epochs=3)

model.save_pretrained('./fine_tuned_unixcoder')
tokenizer.save_pretrained('./fine_tuned_unixcoder')

