#!/bin/bash
set -eo pipefail

MAN=$(cat <<-END
Usage: $0 [--skip-ui <SKIP_UI: boolean>] [--skip-grpc <SKIP_GRPC: boolean>]

List of available options:

    --skip-ui               Skips the build process of ui frontend.
                            Default is false.
    --skip-server           Skips the build process of the backend.
                            Default is false.
    --skip-grpc             Skips the update and build process for gRPC proto files. DO NOT use if proto files have been changed.
                            Default is false.
    --skip-ac               Skips AstroCluster clustering service.
                            Default is false.
    --skip-db               Skips building database.
                            Default is false.
END
)

# Load build script configurations
source ./docker-compose.config.sh
source ./proto/grpc.config.sh

# Colors for terminal
readonly BLUE="\033[0;34m"
readonly RED="\033[0;31m"
readonly NC="\033[0m"

SKIP_UI=false
SKIP_SERVER=false
SKIP_GRPC=false
SKIP_AC=false
SKIP_DB=false
while [[ "$#" -gt 0 ]]; do
    if [[ "$1" = "--skip-ui" ]]; then
        SKIP_UI=true
    elif [[ "$1" = "--skip-server" ]]; then
        SKIP_SERVER=true
    elif [[ "$1" = "--skip-grpc" ]]; then
        SKIP_GRPC=true
    elif [[ "$1" = "--skip-ac" ]]; then
        SKIP_AC=true
    elif [[ "$1" = "--skip-db" ]]; then
        SKIP_DB=true
    elif [[ "$1" = "--help" || "$1" = "-h" ]]; then
        echo "$MAN"
        exit 0
    fi
    shift
done

# gRPC generation
AC_MAVEN_PROFILES="" # Flag for holding various dynamic maven profiles
AC_GENERATE_GRPC=""  # Flag to indicate gRPC generation to clustering service Dockerfile
if [ $SKIP_GRPC = false ]; then
    AC_MAVEN_PROFILES+="grpc"
    AC_GENERATE_GRPC=true
    # Temporarily copy .proto files to cluster and server directories (in order to be visible for copying via Dockerfile)
    cp ./proto/cluster.proto ./server/cluster.proto
    cp ./proto/cluster.proto ./cluster/cluster.proto
fi
export MAVEN_PROFILES="${AC_MAVEN_PROFILES}"
export GENERATE_GRPC="${AC_GENERATE_GRPC}"

# Services to build and deploy
declare -a SERVICES_TO_BUILD
if [ $SKIP_AC = false ]; then
    SERVICES_TO_BUILD+=("clusterservice")
fi
if [ $SKIP_DB = false ]; then
    SERVICES_TO_BUILD+=("db")
    # Undeploy DB and remove docker volume with DB data
    echo -e "${BLUE}$(sudo docker stop ac-db && sudo docker rm ac-db)${NC}"
    echo -e "${BLUE}$(sudo docker volume rm pg_data_ac_cluster)${NC}"
fi
if [ $SKIP_SERVER = false ]; then
    SERVICES_TO_BUILD+=("server")
fi
if [ $SKIP_UI = false ]; then
    SERVICES_TO_BUILD+=("client")
fi

if [ "${#SERVICES_TO_BUILD[@]}" -eq 0 ]; then
    echo -e "[${RED}BUILD ERROR${NC}]: Services to build cannot be zero. Please remove a --skip flag to continue..."
    exit 1
fi
echo -e "[${BLUE}BUILD INFO${NC}]: Building ${SERVICES_TO_BUILD[@]}"
sudo docker-compose up -d --build "${SERVICES_TO_BUILD[@]}"

# Copy generated gRPC files from containers to the actual project directory
if [ $SKIP_GRPC = false ]; then
    # Delete temporary .proto files
    rm -rf ./server/cluster.proto
    rm -rf ./cluster/cluster.proto

    # Copy generated files from containers to actual project directory
    docker cp "${SERVER_CONTAINER_ID}:${BE_GRPC_DOCKER_PATH}" "${BE_GRPC_PROJECT_PATH}"
    echo -e "[${BLUE}INFO${NC}]: Server gRPC update finished. Copied docker gRPC files to project directory."
    docker cp "${CLUSTER_SERVICE_CONTAINER_ID}:${CS_GRPC_DOCKER_PATH}" "${CS_GRPC_PROJECT_PATH}"
    echo -e "[${BLUE}INFO${NC}: Cluster service gRPC update finished. Copied docker gRPC files to project directory."
fi
