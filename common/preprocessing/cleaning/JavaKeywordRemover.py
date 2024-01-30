import javalang
from typing import List

from common.preprocessing.cleaning.KeywordRemover import KeywordRemover

class JavaKeywordRemover(KeywordRemover):
    def __init__(self) -> None:
        super().__init__()

        # Usage of dictionary for faster search of keywords
        self.keywords = {
            "abstract": True, "assert": True,
            "boolean": True, "break": True,
            "byte": True, "case": True,
            "catch": True, "char": True,
            "class": True, "const": True,
            "continue": True, "default": True,
            "do": True, "double": True,
            "else": True, "enum": True,
            "extends": True, "final": True,
            "finally": True, "float": True,
            "for": True, "if": True,
            "implements": True, "import": True,
            "instanceof": True, "int": True,
            "interface": True, "long": True,
            "native": True, "new": True,
            "package": True, "private": True,
            "protected": True, "public": True,
            "return": True, "short": True,
            "static": True, "strictfp": True,
            "super": True, "switch": True,
            "synchronized": True, "this": True,
            "throw": True, "throws": True,
            "transient": True, "try": True,
            "void": True, "volatile": True,
            "while": True, "var": True
        }


    def remove_keywords(self, file_tokens: List[str]) -> List[str]:
        cleaned_keywords = []

        for token in file_tokens:
            if token.value not in self.keywords:
                cleaned_keywords.append(token.value)

        return cleaned_keywords

