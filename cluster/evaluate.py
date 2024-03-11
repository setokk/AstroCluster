import sys
import copy

import numpy as np
from matplotlib import pyplot as plt

import javalang


# <-- MACHINE LEARNING DEPENDENCIES -->
# Preprocessing
from sklearn.model_selection import train_test_split
from sklearn.decomposition import PCA

# Clustering
from sklearn.cluster import MeanShift, AffinityPropagation, Birch

# Metrics
from sklearn.metrics import silhouette_score, calinski_harabasz_score, davies_bouldin_score


# <-- PROJECT DEPENDENCIES -->
from common.cli.validator import validate_arguments
import common.preprocessing.traversing.project_traverser as project_traverser

# Preprocessing
from evaluation.embedding.UniXEmbedder import UniXEmbedder
from evaluation.embedding.Word2VecEmbedder import Word2VecEmbedder
from evaluation.embedding.Doc2VecEmbedder import Doc2VecEmbedder

from common.supported_languages import SUPPORTED_LANGUAGES


# Class used for easier evaluation
# Depending on the embedding method, we might want to use different input data (self.X)
class EmbeddingMethod():
    def __init__(self, emb_name, emb_method, emb_X) -> None:
        self.emb_name = emb_name
        self.emb_method = emb_method
        self.emb_X = emb_X


# Plot to show how similar the data is
def show_data_plot(emb_name: str, pca_embeddings, n_components: int) -> None:
    plt.figure(figsize=(8, 6))
    plt.scatter(np.array(pca_embeddings)[:,0], 
                np.array(pca_embeddings)[:,1], 
                c=[], cmap='viridis', edgecolors='k', alpha=0.7)
    plt.title(f'First two dimensions of data using PCA n_components = {n_components} ({emb_name})')
    plt.xlabel('Dimension 1')
    plt.ylabel('Dimension 2')
    plt.show()

# Function to show cumulative explained variance for PCA
def show_pca_variance_plot(embeddings):
    pca = PCA(n_components = None) # None keeps all components
    X_pca = pca.fit_transform(embeddings)

    # Calculate explained variance ratio
    explained_variance = pca.explained_variance_ratio_

    # Calculate cumulative explained variance
    cumulative_variance = np.cumsum(explained_variance)

    # Create scree plot
    plt.figure(figsize=(8, 6))

    plt.bar(range(len(explained_variance)), explained_variance, alpha=0.5,
    align='center', label='individual explained variance', color='g')
    plt.step(range(len(cumulative_variance)), cumulative_variance, where='mid',
    label='cumulative explained variance')
    plt.ylabel('Explained variance ratio')
    plt.xlabel('Principal components')
    plt.legend(loc='best')
    plt.tight_layout()
    plt.show()

# Function to get the pca embeddings
def get_pca_embeddings(emb_name: str, embeddings):
        n_components = None
        if emb_name == 'UniXcoder':
            n_components = 100
        elif emb_name == 'Word2VecEmbedder':
            n_components = 10
        elif emb_name == 'Doc2VecEmbedder':
            n_components = 125

        pca = PCA(n_components=n_components)
        pca_embeddings = pca.fit_transform(embeddings)
        
        return pca_embeddings, n_components

# Function to display and save performance metrics
def calc_performance_metrics(X, Y_pred) -> dict:
    metrics_dict = {}

    # Silhouette Score
    metrics_dict['Silhouette Score'] = silhouette_score(X, Y_pred)

    # Calinski-Harabasz Index
    metrics_dict['Calinski-Harabasz Index'] = calinski_harabasz_score(X, Y_pred)

    # Davies-Bouldin Index
    metrics_dict['Davies-Bouldin Index'] = davies_bouldin_score(X, Y_pred)

    return metrics_dict
    

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
    files, file_paths = project_traverser.get_project_files(traverser_strategy,
                                                            project_path, 
                                                            file_extensions)
    
    # Get file tokens (used in certain embedding methods)
    tokenized_files = []
    for file_content in files:
        tokens = list(javalang.tokenizer.tokenize(file_content))
        token_values = [token.value for token in tokens]
        tokenized_files.append(token_values)

    # Define embedding methods
    embedding_methods = [
        EmbeddingMethod("UniXcoder", UniXEmbedder(), files),
        EmbeddingMethod("Word2VecEmbedder", Word2VecEmbedder(), tokenized_files),
        EmbeddingMethod("Doc2VecEmbedder", Doc2VecEmbedder(), tokenized_files)
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
        pca_embeddings, n_components = get_pca_embeddings(emb_name, embeddings)

        # Here we declare a list that holds:
        # a) original embeddings
        # b) pca embeddings
        embeddings_list = []
        embeddings_list.append(embeddings)
        embeddings_list.append(pca_embeddings)

        # Show variance plot to find out the optimal n_components
        show_pca_variance_plot(embeddings)

        # Show first two dimensions with PCA (with optimal n_components)
        show_data_plot(emb_name, pca_embeddings, n_components)

        # Try each embedding
        for embedding in embeddings_list:
            print(f'{emb_name}')
            print(f'Dimension: {len(embedding[0])}')

            X_train, X_test, train_indices, test_indices = train_test_split(
                embedding, range(len(emb_X)),
                test_size=0.2, random_state=42
            )
            for cl_name in clustering_methods:
                cl_model = copy.deepcopy(clustering_methods[cl_name])
                cl_model.fit(X_train)
                Y_pred = cl_model.predict(X_test)

                num_of_clusters = len(np.unique(Y_pred))
                metrics_dict = calc_performance_metrics(X_test, Y_pred)
                print(f'\n{emb_name} -> {cl_name}')
                print(f'Number of clusters: {num_of_clusters}')
                print(f'Metrics:')
                print(metrics_dict)
                print("---------------------\n\n")
                #original_file_content = files[test_indices[i]]
