from common.preprocessing.traversing.JavaTraverserStrategy import JavaTraverserStrategy
from common.preprocessing.cleaning.JavaKeywordRemover import JavaKeywordRemover

SUPPORTED_LANGUAGES = {
    'java': (JavaTraverserStrategy(), JavaKeywordRemover())
}