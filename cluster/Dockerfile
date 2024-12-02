FROM python:3.11-slim

WORKDIR /ac-clustering-service

# Docker cache
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
RUN pip install gdown

COPY . .

ARG GENERATE_GRPC
RUN if [ -n "${GENERATE_GRPC}" ]; then python -m grpc_tools.protoc -I . --python_out=./service --grpc_python_out=./service ./cluster.proto; fi
ENV PYTHONPATH=/ac-clustering-service

RUN chmod +x init.sh
ENTRYPOINT ["./init.sh"]
