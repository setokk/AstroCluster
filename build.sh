#!/bin/bash
MAN=$(cat <<-END
Usage: $0 [--skip-ui <SKIP_UI: boolean>] [--skip-proto <SKIP_PROTO: boolean>]

List of available options:

    --skip-ui               Skips the build process of ui frontend.
                            Default is false.
    --skip-proto            Skips the update and build process for gRPC proto files. DO NOT use if proto files have been changed.
                            Default is false.
END
)

SKIP_UI=false
SKIP_PROTO=false
while [[ "$#" -gt 0 ]]; do
    if [[ "$1" = "--skip-ui" ]]; then
        SKIP_UI=true
    elif [[ "$1" = "--skip-proto" ]]; then
        SKIP_PROTO=true
    elif [[ "$1" = "--help" || "$1" = "-h" ]]; then
        echo "$MAN"
        exit 0
    fi
    shift
done
