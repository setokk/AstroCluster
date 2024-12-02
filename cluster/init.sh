#!/bin/bash
# Download model if necessary
DOWNLOAD_MODEL=$1
ASTROCLUSTER_MODEL_PATH=$2
if $DOWNLOAD_MODEL; then
    cd "${ASTROCLUSTER_MODEL_PATH}"
    gdown --folder "https://drive.google.com/drive/folders/12vNaQkrtLhLSJmB5dan3jRjIXdNoUYuw"
fi

cd /ac-clustering-service
python "./service/cluster_grpc_server.py"