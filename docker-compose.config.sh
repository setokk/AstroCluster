#/bin/bash
set -eo pipefail

#
# Configuration file for docker compose properties, containers, variables etc.
#
readonly ENV_FILE=".env"
echo "# Auto-generated .env configuration file used by docker-compose.yml" > "$ENV_FILE"

# Configuration starts here
{
	export SERVER_CONTAINER_ID="ac-server"                   && echo "SERVER_CONTAINER_ID=${SERVER_CONTAINER_ID}"
	export CLUSTER_SERVICE_CONTAINER_ID="ac-cluster-service" && echo "CLUSTER_SERVICE_CONTAINER_ID=${CLUSTER_SERVICE_CONTAINER_ID}"
	export CLIENT_CONTAINER_ID="ac-client"                   && echo "CLIENT_CONTAINER_ID=${CLIENT_CONTAINER_ID}"
	export DB_CONTAINER_ID="ac-db"                           && echo "DB_CONTAINER_ID=${DB_CONTAINER_ID}"
} >> "${ENV_FILE}"
echo "Generated/Overwritten .env file"
