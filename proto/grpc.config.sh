#/bin/bash
set -eo pipefail

#
# Configuration file for gRPC generation.
#
# The following paths are used for copying generated gRPC files from docker containers to the actual project directory.
# Project paths must be relative to build.sh.
export BE_GRPC_PROJECT_PATH="./server/src/main/java/edu/setokk/astrocluster"
export CS_GRPC_PROJECT_PATH="./cluster"
export BE_GRPC_DOCKER_PATH="/ac-server/src/main/java/edu/setokk/astrocluster/grpc"
export CS_GRPC_DOCKER_PATH="/ac-clustering-service/service"
