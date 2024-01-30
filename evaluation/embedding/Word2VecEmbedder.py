import numpy as np
from gensim.models import Word2Vec
from evaluation.embedding.Embedder import Embedder

class Word2VecEmbedder(Embedder):
    def __init__(self) -> None:
        super().__init__()
        self.model = None

    def get_embeddings(self, files):
        if self.model == None:
            raise RuntimeError('Attempted to get embeddings from an UNTRAINED model.')

        # Get the embeddings for each file and for each of its tokens
        # After retrieving the embeddings of its tokens,
        # calculate the mean of them and use the mean instead
        tokenized_files = files
        embeddings = []
        for file_tokens in tokenized_files:
            file_embeddings = []
            for token in file_tokens:
                token_embedding = self.model.wv[token]
                file_embeddings.append(token_embedding)
            
            file_embeddings = np.array(file_embeddings)
            mean_embedding = np.mean(file_embeddings, axis=0)
            embeddings.append(mean_embedding.tolist())
        
        return embeddings

    def train(self, files) -> None:
        print('Starting Word2Vec embedder training...')
        
        tokenized_files = files
        self.model = Word2Vec(sentences=tokenized_files, 
                              vector_size=100, window=5, 
                              min_count=1, workers=4)
        
        print('Ended Word2Vec embedder training')