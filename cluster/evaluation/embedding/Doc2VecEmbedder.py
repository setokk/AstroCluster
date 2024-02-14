from evaluation.embedding.Embedder import Embedder

class Doc2VecEmbedder(Embedder):
    def __init__(self) -> None:
        super().__init__()

    def get_embeddings(self, files):
        return super().get_embeddings()
    
    def train(self, files) -> None:
        return super().train(files)