import common.preprocessing.traversing.project_traverser as project_traverser
import javalang
import numpy as np
import sys

# <-- PROJECT DEPENDENCIES -->
from common.cli.validator import validate_arguments
from common.supported_languages import SUPPORTED_LANGUAGES

# Preprocessing
from evaluation.embedding.Word2VecEmbedder import Word2VecEmbedder

# <-- MACHINE LEARNING DEPENDENCIES -->
# Preprocessing
from sklearn.model_selection import train_test_split
# Clustering
from sklearn.cluster import MeanShift

if __name__ == '__main__':
    # Validate arguments
    is_valid, project_language, project_path, file_extensions = validate_arguments(sys.argv)
    if not is_valid:
        print('Error in input, exiting...')
        exit(1)

    # Check for language support
    if project_language not in SUPPORTED_LANGUAGES:
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
    embedder = Word2VecEmbedder()
    clustering_method = MeanShift(max_iter=650, n_jobs=-1)
    emb_X = tokenized_files

    embedder.train(emb_X)
    embeddings = embedder.get_embeddings(emb_X)

    print('Word2VecEmbedder')
    print(f'Dimension: {len(embeddings[0])}')

    clustering_method.fit(embeddings)

    print('\nWord2VecEmbedder -> MeanShift---------------------\n\n')
