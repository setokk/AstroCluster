FROM python:3.11-slim

WORKDIR /ac-clustering-service
COPY . .

RUN pip install --no-cache-dir -r requirements.txt

CMD ["python", "./service/cluster_grpc_server.py"]
