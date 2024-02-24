from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from evaluation.embedding.Embedder import Embedder

class Doc2VecEmbedder(Embedder):
    def __init__(self, vector_size=300, 
                 window=5, min_count=1, 
                 workers=4, epochs=100) -> None:
        super().__init__()
        self.model = None
        self.vector_size = vector_size
        self.window = window
        self.min_count = min_count
        self.workers = workers
        self.epochs = epochs

    def get_embeddings(self, files):
        if self.model is None:
            raise ValueError("The model needs to be trained first.")

        tagged_data = [TaggedDocument(words=file_content, tags=[str(i)]) for i, file_content in enumerate(files)]
        embeddings = [self.model.infer_vector(tagged_file.words) for tagged_file in tagged_data]
        return embeddings
    
    def train(self, files) -> None:
        tagged_data = [TaggedDocument(words=document, tags=[str(i)]) for i, document in enumerate(files)]

        self.model = Doc2Vec(vector_size=self.vector_size, 
                             window=self.window, 
                             min_count=self.min_count, 
                             workers=self.workers, 
                             epochs=self.epochs)
        self.model.build_vocab(tagged_data)
        self.model.train(tagged_data, total_examples=self.model.corpus_count, epochs=self.model.epochs)
