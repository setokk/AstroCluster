#/bin/bash
set -eo pipefail

#
# Configuration file for terminal colors.
#
# This file is used in order to eliminate copy-pasting the color values everywhere in all scripts.
# Scripts can simply import this file and get the colors automatically.
readonly RED="\033[0;31m"
readonly GREEN='\033[0;32m'
readonly BLUE="\033[0;34m"
readonly NC="\033[0m"