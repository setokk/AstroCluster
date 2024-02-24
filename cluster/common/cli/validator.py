import ast
import re

from typing import List, Tuple


def validate_arguments(argv: List[str]) -> Tuple[bool, str, str, List[str]]:
    # Project path in command line arguments
    if (len(argv) != 4):
        print('Incorrect number of arguments!\n')
        print('Usage:')
        print('\tpython3 program.py <project_language: str> <project_path: str> <file_extensions: List[str]>')
        print('\tExample: python3 train.py "java" "C:/users/my project" "[\'java\', \'py\', \'c\', \'erl\']"')
        return False, '', '', []
    
    # Get project language
    project_language = argv[1].strip()

    # Get project path
    project_path = argv[2].strip()

    # Parse list argument to get included file extensions
    file_extensions_arg = argv[3].strip()

    # Check if file extensions list is in correct format (simple check)
    list_regex = r'^[.+]$'
    if (re.fullmatch(list_regex, file_extensions_arg)):
        print('Incorrect list format. Please use: "[item_1, item_2, item_3, ..., item_n]"')
        return False, '', '', []

    # Remove the leading and trailing whitespaces and convert it into a list using ast.literal_eval
    file_extensions = ast.literal_eval(file_extensions_arg)

    return True, project_language, project_path, file_extensions