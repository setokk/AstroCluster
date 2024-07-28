from concurrent import futures
import grpc
import cluster_pb2
import cluster_pb2_grpc

class ClusterServiceServicer(cluster_pb2_grpc.ClusterServiceServicer):
    def PerformClustering(self, request, context):
        print(f"Received request: path={request.path}, lang={request.lang}, extensions={request.extensions}")

        response = cluster_pb2.ClusterResponse(status=0)
        return response

def serve() -> None:
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    cluster_pb2_grpc.add_ClusterServiceServicer_to_server(ClusterServiceServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    print("Server started on port 50051")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()