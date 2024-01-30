import sys

import numpy as np
from matplotlib import pyplot as plt

import javalang

# Machine Learning dependencies
from sklearn.model_selection import train_test_split
from sklearn.cluster import MeanShift, AffinityPropagation, Birch

# Project dependencies
from common.input.validator import validate_arguments
import common.preprocessing.traversing.project_traverser as project_traverser

# Preprocessing
from evaluation.embedding.UniXEmbedder import UniXEmbedder
from evaluation.embedding.Word2VecEmbedder import Word2VecEmbedder
from evaluation.embedding.Doc2VecEmbedder import Doc2VecEmbedder

from common.supported_languages import SUPPORTED_LANGUAGES


# Class used for cleaner evaluation
# Depending on the embedding method, we might want to use different input data (self.X)
class EmbeddingMethod():
    def __init__(self, emb_name, emb_method, emb_X) -> None:
        self.emb_name = emb_name
        self.emb_method = emb_method
        self.emb_X = emb_X


# Plot to show how similar the data is
def show_data_plot(embeddings):
    plt.figure(figsize=(8, 6))
    plt.scatter(np.array(embeddings)[:,0], 
                np.array(embeddings)[:,1], 
                c=[], cmap='viridis', edgecolors='k', alpha=0.7)
    plt.title(f'Scatter plot of data')
    plt.xlabel('Dimension 1')
    plt.ylabel('Dimension 2')
    plt.show()

# Function to display and save performance metrics
def calc_performance_metrics(X, Y_pred):
    pass


if __name__ == '__main__':
    # Validate arguments
    is_valid, project_language, project_path, file_extensions = validate_arguments(sys.argv)
    if (not is_valid):
        print('Error in input, exiting...')
        exit(1)

    # Check for language support
    if (project_language not in SUPPORTED_LANGUAGES):
        print(f'Support for language"{project_language}" does not exist. List of supported languages:\n')
        for language in SUPPORTED_LANGUAGES.keys:
            print(f'\t{language}')
        exit(1)

    # Get project traverser strategy based on language
    traverser_strategy, keyword_remover = SUPPORTED_LANGUAGES.get(project_language)

    # Get file content data
    files,file_paths = project_traverser.get_project_files(traverser_strategy,  
                                                           project_path, 
                                                           file_extensions)

    # Get file tokens for certain embedding methods
    tokenized_files = []
    for file_content in files:
        tokens = list(javalang.tokenizer.tokenize(file_content))
        token_values = [token.value for token in tokens]
        tokenized_files.append(token_values)
    
    # Define embedding methods
    embedding_methods = [
        EmbeddingMethod("UniXcoder", UniXEmbedder(), files),
        EmbeddingMethod("Word2VecEmbedder", Word2VecEmbedder(), tokenized_files)
        #EmbeddingMethod("Doc2VecEmbedder", Doc2VecEmbedder(), tokenized_files)
    ]

    # Instantiate clustering algorithms
    clustering_methods = {
        "MeanShift": MeanShift(max_iter=650, n_jobs=-1),
        "Affinity Propagation": AffinityPropagation(),
        "BIRCH": Birch()
    }

    # Try every combination to compare metrics
    for embedder in embedding_methods:
        emb_name   = embedder.emb_name
        emb_method = embedder.emb_method
        emb_X      = embedder.emb_X

        emb_method.train(emb_X)
        embeddings = emb_method.get_embeddings(emb_X)
        show_data_plot(embeddings)

        X_train, X_test, train_indices, test_indices = train_test_split(
            embeddings, range(len(emb_X)),
            test_size=0.2, random_state=42
        )
        for cl_name in clustering_methods:
            cl_method = clustering_methods[cl_name]
            cl_method.fit(X_train)
            Y_pred = cl_method.predict(X_test)

            print(f'\n{emb_name} -> {cl_name}')
            for i, label in enumerate(Y_pred):
                original_file = files[test_indices[i]]
                print(f'Datapoint {i}, label: {label}')