import glob

from typing import List
from typing import Tuple

# Project dependencies
from common.preprocessing.traversing.TraverserStrategy import TraverserStrategy


def get_project_files(traverser_strategy: TraverserStrategy,
                      project_path: str, 
                      file_extensions: List[str]) -> Tuple[List[str], List[str]]:
    # Files to be returned
    files = []
    file_paths = []

    # Create regex for valid extensions
    valid_extensions = r'\.(' + '|'.join(file_extensions) + r')$'
    for file_path in glob.iglob(project_path + '/**', recursive=True):
        if (not traverser_strategy.check_file_name(file_path, valid_extensions)):
            continue
        try:
            with open(file_path, 'r') as f:
                file_content = f.read()
                if (traverser_strategy.check_file_content(file_content)):
                    files.append(file_content)
                    file_paths.append(file_path)
        except Exception as e:
            print(f'Error reading file {file_path}: {e}')

    return files, file_paths
