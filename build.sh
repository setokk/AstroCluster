#!/bin/bash
set -eo pipefail

MAN=$(cat <<-END
Usage: $0 [--skip-ui <SKIP_UI: boolean>]     [--skip-server <SKIP_SERVER: boolean>]
                  [--skip-grpc <SKIP_GRPC: boolean>] [--skip-ac <SKIP_AC: boolean>]
                  [--skip-db <SKIP_DB: boolean>]

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

# Helper functions
undeploy_all_services() {
	echo -e "[${BLUE}BUILD INFO${NC}]: Undeploying ${DB_CONTAINER_ID} ($(sudo docker stop ${DB_CONTAINER_ID} && sudo docker rm ${DB_CONTAINER_ID}))"
	echo -e "[${BLUE}BUILD INFO${NC}]: Undeploying ${SERVER_CONTAINER_ID} ($(sudo docker stop ${SERVER_CONTAINER_ID} && sudo docker rm ${SERVER_CONTAINER_ID}))"
	echo -e "[${BLUE}BUILD INFO${NC}]: Undeploying ${CLUSTER_SERVICE_CONTAINER_ID} ($(sudo docker stop ${CLUSTER_SERVICE_CONTAINER_ID} && sudo docker rm ${CLUSTER_SERVICE_CONTAINER_ID}))"
	echo -e "[${BLUE}BUILD INFO${NC}]: Undeploying ${CLIENT_CONTAINER_ID} ($(sudo docker stop ${CLIENT_CONTAINER_ID} && sudo docker rm ${CLIENT_CONTAINER_ID}))"
}

update_dynamic_env_variables() {
    # Append all dynamic environment variables to docker-compose .env file before building
    {
        echo "MAVEN_PROFILES=${AC_MAVEN_PROFILES}"
        echo "GENERATE_GRPC=${AC_GENERATE_GRPC}"
        echo "DOWNLOAD_MODEL=${DOWNLOAD_MODEL}"
    } >> "${DOCKER_COMPOSE_ENV_FILE}"
}

build_services() {
  if [ "${#SERVICES_TO_BUILD[@]}" -eq 0 ]; then
      echo -e "[${RED}BUILD ERROR${NC}]: Services to build cannot be zero. Please remove a --skip flag to continue..."
      exit 1
  fi
  echo -e "[${BLUE}BUILD INFO${NC}]: Building ${GREEN}${SERVICES_TO_BUILD[@]}${NC}"
  sudo docker-compose up -d --build "${SERVICES_TO_BUILD[@]}"
}

# Main script
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

# Imports
source ./docker-compose.config.sh
source ./colors.config.sh

# gRPC generation
AC_MAVEN_PROFILES="" # Flag for holding various dynamic maven profiles
AC_GENERATE_GRPC=""  # Flag to indicate gRPC generation to clustering service Dockerfile
if [ $SKIP_GRPC = false ]; then
    AC_MAVEN_PROFILES+="grpc"
    AC_GENERATE_GRPC=true
    # Temporarily copy .proto files to cluster and server directories (in order to be visible for copying via Dockerfile)
    mkdir ./server/default && mkdir ./server/custom
    cp ./proto/cluster.proto ./server/default/cluster.proto && echo -e "[${BLUE}BUILD INFO${NC}]: Copied cluster.proto in server"
    cp ./proto/cluster.proto ./server/custom/cluster.proto && echo -e "[${BLUE}BUILD INFO${NC}]: Copied cluster.proto in server custom"
    cp ./proto/cluster.proto ./cluster/cluster.proto && echo -e "[${BLUE}BUILD INFO${NC}]: Copied cluster.proto in cluster"
fi

# Figure out whether to download model (only if it does not exist locally)
# First, figure if ac-clustering-service exists
DOWNLOAD_MODEL=true
CLUSTER_SERVICE_EXISTS=$(sudo docker ps -q -f name="${CLUSTER_SERVICE_CONTAINER_ID}")
if [ -n "${CLUSTER_SERVICE_EXISTS}" ]; then
    MODEL_EXISTS=$(sudo docker exec "${CLUSTER_SERVICE_CONTAINER_ID}" sh -c "test -d ${ASTROCLUSTER_MODEL_PATH} && echo 'exists' || echo 'does not exist'")
    if [ "${MODEL_EXISTS}" == "does not exist" ]; then
        DOWNLOAD_MODEL=false
    fi
fi

# Services to build and deploy
declare -a SERVICES_TO_BUILD
if [ $SKIP_AC = false ]; then
    SERVICES_TO_BUILD+=("clusterservice")
fi
if [ $SKIP_DB = false ]; then
    SERVICES_TO_BUILD+=("db")
    # Remove docker volumes with DB data
    undeploy_all_services
    echo -e "[${BLUE}BUILD INFO${NC}]: Removed volume $(sudo docker volume rm pg_data_ac_cluster)"
    echo -e "[${BLUE}BUILD INFO${NC}]: Removed volume $(sudo docker volume rm shared_cloned_projects)"
fi
if [ $SKIP_SERVER = false ]; then
    SERVICES_TO_BUILD+=("server")
fi
if [ $SKIP_UI = false ]; then
    SERVICES_TO_BUILD+=("client")
fi

update_dynamic_env_variables
build_services

# Copy generated gRPC files from containers to the actual project directory
if [ $SKIP_GRPC = false ]; then
    # Delete temporary .proto files
    sudo rm -rf ./server/default
    sudo rm -rf ./server/custom
    sudo rm -rf ./cluster/cluster.proto

    # Copy generated files from containers to actual project directory
    sudo docker cp "${SERVER_CONTAINER_ID}:${BE_GRPC_DOCKER_PATH}" "${BE_GRPC_PROJECT_PATH}"
    echo -e "[${BLUE}BUILD INFO${NC}]: Server gRPC update finished. Copied docker gRPC files to project directory."
    sudo docker cp "${CLUSTER_SERVICE_CONTAINER_ID}:${CS_GRPC_DOCKER_PATH}" "${CS_GRPC_PROJECT_PATH}" && sudo rm -rf "${CS_GRPC_PROJECT_PATH}/service/__pycache__"
    echo -e "[${BLUE}BUILD INFO${NC}: Cluster service gRPC update finished. Copied docker gRPC files to project directory."

    # Change ownership of files since they were created via root user inside containers
    sudo chown -R setokk:setokk "${BE_GRPC_PROJECT_PATH}"
    sudo chown -R setokk:setokk "${CS_GRPC_PROJECT_PATH}"
fi
