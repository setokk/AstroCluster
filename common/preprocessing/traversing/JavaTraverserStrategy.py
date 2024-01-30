import re

from common.preprocessing.traversing.TraverserStrategy import TraverserStrategy

class JavaTraverserStrategy(TraverserStrategy):
    def __init__(self):
        pass

    def check_file_name(self, file_path: str, valid_extensions: str) -> bool:
        return (re.search(valid_extensions, file_path) and 
                file_path.find('module-info.java') == -1 and
                file_path.find('Test') == -1 and 
                file_path.find('test') == -1)

    def check_file_content(self, file_content: str) -> bool:
        return ('public static void main' not in file_content)