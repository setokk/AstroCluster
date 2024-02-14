# Strategy pattern to support different languages
class TraverserStrategy:
    def __init__(self):
        pass

    def check_file_name(self, file_path: str) -> bool:
        pass

    def check_file_content(self, file_content: str) -> bool:
        pass