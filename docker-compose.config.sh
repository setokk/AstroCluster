#/bin/bash
set -eo pipefail

# Import other configs
source ./proto/grpc.config.sh

#
# Configuration file for docker compose properties, containers, variables etc.
#
export DOCKER_COMPOSE_ENV_FILE=".env"
echo "# Auto-generated .env configuration file used by docker-compose.yml" > "${DOCKER_COMPOSE_ENV_FILE}"

# Config starts here
export SERVER_CONTAINER_ID="ac-server"
export CLUSTER_SERVICE_CONTAINER_ID="ac-cluster-service"
export CLIENT_CONTAINER_ID="ac-client"
export DB_CONTAINER_ID="ac-db"
export ASTROCLUSTER_MODEL_PATH="/ac-clustering-service/finetune"
{
  # Section of environment variables that are exported in this script file
	echo "SERVER_CONTAINER_ID=${SERVER_CONTAINER_ID}"
	echo "CLUSTER_SERVICE_CONTAINER_ID=${CLUSTER_SERVICE_CONTAINER_ID}"
	echo "CLIENT_CONTAINER_ID=${CLIENT_CONTAINER_ID}"
	echo "DB_CONTAINER_ID=${DB_CONTAINER_ID}"
	echo "ASTROCLUSTER_MODEL_PATH=${ASTROCLUSTER_MODEL_PATH}"

	# Section of environment variables that are exported elsewhere
	echo "BE_GRPC_DOCKER_PATH=${BE_GRPC_DOCKER_PATH}"
} >> "${DOCKER_COMPOSE_ENV_FILE}"
echo "Generated/Overwritten .env file"
