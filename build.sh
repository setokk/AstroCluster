#!/bin/bash
MAN=$(cat <<-END
Usage: $0 [--skip-ui <SKIP_UI: boolean>] [--skip-grpc <SKIP_GRPC: boolean>]

List of available options:

    --skip-ui               Skips the build process of ui frontend.
                            Default is false.
    --skip-grpc             Skips the update and build process for gRPC proto files. DO NOT use if proto files have been changed.
                            Default is false.
END
)

SKIP_UI=false
SKIP_GRPC=false
while [[ "$#" -gt 0 ]]; do
    if [[ "$1" = "--skip-ui" ]]; then
        SKIP_UI=true
    elif [[ "$1" = "--skip-grpc" ]]; then
        SKIP_GRPC=true
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
SERVICES_TO_BUILD=("db" "clusterservice" "server")
if [ $SKIP_UI = false ]; then
    SERVICES_TO_BUILD+=("client")
fi

sudo docker-compose up -d --build "${SERVICES_TO_BUILD[@]}"