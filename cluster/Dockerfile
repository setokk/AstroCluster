FROM python:3.11-slim

WORKDIR /ac-clustering-service
COPY . .

RUN pip install --no-cache-dir -r requirements.txt

ARG GENERATE_GRPC
RUN if [ -n $GENERATE_GRPC ]; then python -m grpc_tools.protoc -I . --python_out=. --grpc_python_out=. ./cluster.proto; fi

CMD ["python", "./service/cluster_grpc_server.py"]
