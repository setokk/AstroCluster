from concurrent import futures
import grpc
import torch
from transformers import RobertaTokenizer
from sklearn.cluster import AffinityPropagation
from cluster_pb2 import ClusterResponse
import cluster_pb2_grpc as cluster_pb2_grpc
import logging

import common.preprocessing.traversing.project_traverser as project_traverser
from common.supported_languages import SUPPORTED_LANGUAGES
from unixcoder_embedding import UniXcoderForEmbedding

class ClusterServiceServicer(cluster_pb2_grpc.ClusterServiceServicer):
    def __init__(self, model, tokenizer, device, clustering_method=AffinityPropagation()):
        self.model = model
        self.tokenizer = tokenizer
        self.device = device
        self.clustering_method = clustering_method

    def PerformClustering(self, request, context):
        logging.info(f"Received request: path={request.path}, lang={request.lang}, extensions={request.extensions}")

        # Get project traverser strategy based on language
        traverser_strategy, keyword_remover = SUPPORTED_LANGUAGES.get(request.lang)

        # Get file content data
        files, file_paths = project_traverser.get_project_files(traverser_strategy,
                                                                f"/ac-clustering-service/projects/{request.path}",
                                                                request.extensions)

        # Retrieve embeddings
        inputs_ids = self.tokenizer(files, return_tensors='pt', padding=True, truncation=True, max_length=512)
        inputs_ids = inputs_ids['input_ids'].to(self.device)
        with torch.no_grad():
            embeddings = self.model(inputs_ids)

        # Pass the embeddings to clustering method
        cluster_labels = self.clustering_method.fit_predict(embeddings.cpu())

        response = ClusterResponse(clusterLabels=cluster_labels.tolist(), filePaths=file_paths)
        logging.info(f"Exiting request: path={request.path}, lang={request.lang}, extensions={request.extensions}")
        return response

def serve() -> None:
    logging.info("sup")
    # Load the fine-tuned model and tokenizer
    model_name = "microsoft/unixcoder-base"
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

    # Load embedder model and tokenizer
    tokenizer = RobertaTokenizer.from_pretrained('./finetune/fine_tuned_unixcoder')
    model = UniXcoderForEmbedding(model_name)
    model.load_state_dict(torch.load('./finetune/fine_tuned_unixcoder/pytorch_model.bin', map_location=device))
    model.to(device)
    model.eval()

    # gRPC server
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    cluster_pb2_grpc.add_ClusterServiceServicer_to_server(ClusterServiceServicer(model, tokenizer, device), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    logging.info("Server started on port 50051")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()