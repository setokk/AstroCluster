#/bin/bash
set -eo pipefail

#
# Configuration file for docker compose properties, containers, variables etc.
#
export SERVER_CONTAINER_ID="ac-server"
export CLUSTER_SERVICE_CONTAINER_ID="ac-cluster-service"
export CLIENT_CONTAINER_ID="ac-client"
export DB_CONTAINER_ID="ac-db"
