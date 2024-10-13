#!/bin/bash
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

# Maven profiles
MAVEN_PROFILES=""
if [ $SKIP_GRPC = false ]; then
    MAVEN_PROFILES+="grpc"
fi
export AC_MAVEN_PROFILES="${MAVEN_PROFILES}"

# Services to build and deploy
declare -a SERVICES_TO_BUILD
if [ $SKIP_AC = false ]; then
    SERVICES_TO_BUILD+=("clusterservice")
fi
if [ $SKIP_DB = false ]; then
    SERVICES_TO_BUILD+=("db")    
fi
if [ $SKIP_SERVER = false ]; then
    SERVICES_TO_BUILD+=("server")
fi
if [ $SKIP_UI = false ]; then
    SERVICES_TO_BUILD+=("client")
fi

if [ "${#SERVICES_TO_BUILD[@]}" -eq 0 ]; then
    echo -e "Services to build cannot be zero. Please remove a --skip service argument to continue..."
    exit 1
fi
echo -e "Building ${SERVICES_TO_BUILD[@]}"
sudo docker-compose up -d --build "${SERVICES_TO_BUILD[@]}"
