from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from evaluation.embedding.Embedder import Embedder

class Doc2VecEmbedder(Embedder):
    def __init__(self) -> None:
        super().__init__()
        self.model = None

    def get_embeddings(self, files):
        if self.model is None:
            raise ValueError("The model needs to be trained first.")

        # Assuming 'files' is a list of documents
        tagged_data = [TaggedDocument(words=document, tags=[str(i)]) for i, document in enumerate(files)]
        embeddings = [self.model.infer_vector(document.words) for document in tagged_data]
        return embeddings
    
    def train(self, files) -> None:
        tagged_data = [TaggedDocument(words=document, tags=[str(i)]) for i, document in enumerate(files)]

        # You can customize the parameters as needed
        self.model = Doc2Vec(vector_size=300, window=5, min_count=1, workers=4, epochs=100)
        self.model.build_vocab(tagged_data)
        self.model.train(tagged_data, total_examples=self.model.corpus_count, epochs=self.model.epochs)
