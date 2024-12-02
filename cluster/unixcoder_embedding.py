from torch.utils.data import Dataset, DataLoader
import torch.nn as nn
from unixcoder import UniXcoder

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