from evaluation.embedding.Embedder import Embedder

from unixcoder import UniXcoder
import torch

class UniXEmbedder(Embedder):
    def __init__(self) -> None:
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model = UniXcoder("microsoft/unixcoder-base")
        self.model.to(self.device)

    def get_embeddings(self, files):
        # Tokenize each file content and get embeddings for tokens
        embeddings_list = []
        for file in files:
            with torch.no_grad():
                # Get file embedding
                tokens_ids = self.model.tokenize([file], max_length=512, mode="<encoder-only>")
                source_ids = torch.tensor(tokens_ids).to(self.device)
                tokens_embeddings, file_embedding = self.model(source_ids)
                
                embeddings_list.append(file_embedding[0].tolist())

        return embeddings_list

    def train(self, files) -> None:
        return super().train(files)