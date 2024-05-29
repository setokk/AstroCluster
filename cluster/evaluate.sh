#!/bin/bash
MAN=$(cat <<-END
Usage: $0 [--clone-mode | -cm <GIT_CLONE_MODE: string>] [--delete-dirs | -d <DELETE_DIRS: boolean>]

List of available options:

    --clone-mode or -cm            Git clone mode. Acceptable values: "ssh" or "https". Default value is "https".
    --delete-dirs or -d            Flag for deleting the cloned project directories after finishing evaluation. Default value is false.
END
)

GIT_CLONE_MODE="https"
DELETE_DIRS=false
while [[ "$#" -gt 0 ]]; do
    if [[ "$1" = "--clone-mode" || "$1" = "-cm" ]]; then
        GIT_CLONE_MODE="$2"
    elif [[ "$1" = "--delete-dirs" || "$1" = "-d" ]]; then
        DELETE_DIRS=true
    elif [[ "$1" = "--help" || "$1" = "-h" ]]; then
        echo "$MAN"
        exit 0
    fi
    shift
done

declare -A projects_dir_map
projects_dir_map["flink"]="./projects/apache/flink"
projects_dir_map["kafka"]="./projects/apache/kafka"
projects_dir_map["tomcat"]="./projects/apache/tomcat"
projects_dir_map["storm"]="./projects/apache/storm"
projects_dir_map["hive"]="./projects/apache/hive"

# Clone projects
for project in "${!projects_dir_map[@]}"; do
    PROJECT_DIR="${projects_dir_map[$project]}"
    if [ "$GIT_CLONE_MODE" = "ssh" ]; then
        echo "Cloning in ssh mode..."
        git clone "git@github.com:apache/$project.git" "$PROJECT_DIR"
    elif [ "$GIT_CLONE_MODE" = "https" ]; then
        echo "Cloning in https mode..."
        git clone "https://github.com/apache/$project.git" "$PROJECT_DIR"
    else
        echo "Clone mode has to be one of the following: [ssh, https]"
        exit 1
    fi
done

# Evaluate projects
for project in "${!projects_dir_map[@]}"; do
    PROJECT_NAME_UPPER=$(echo "$project" | tr '[:lower:]' '[:upper:]')
    PROJECT_DIR="${projects_dir_map[$project]}"
    echo "-------------------[ APACHE $PROJECT_NAME_UPPER ]-------------------"
    python3 -B evaluate.py "java" "$PROJECT_DIR" "['java']"
    echo "-------------------[ APACHE $PROJECT_NAME_UPPER ]-------------------"
    echo ""
done

# Delete directories
if [ $DELETE_DIRS = true ]; then
    for project in "${!projects_dir_map[@]}"; do
        PROJECT_DIR="${projects_dir_map[$project]}"
        rm -rf "$PROJECT_DIR"
        echo -e "Deleted $PROJECT_DIR"
    done
fi
