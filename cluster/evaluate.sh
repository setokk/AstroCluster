#!/bin/bash
MAN=$(cat <<-END
Usage: ./$0 [--clone-mode | -cm <GIT_CLONE_MODE: string>] [--delete-dirs | -d <DELETE_DIRS: boolean>]

List of available options:

    --clone-mode or -cm            Git clone mode. Acceptable values: "ssh" or "https". Default value is "https".
    --delete-dirs or -d            Flag for deleting the cloned project directories after finishing evaluation. Default value is false.
END
)

GIT_CLONE_MODE="https"
DELETE_DIRS=false
while [[ "$#" -gt 0 ]]
do
    if [ ["$1" = "--clone-mode"] || ["$1" = "-cm"] ]; then
        GIT_CLONE_MODE="$2"
    else if [ ["$1" = "--delete-dirs"] || ["$1" = "-d"] ]; then
        DELETE_DIRS=true
    fi
shift
done

declare -A projects_dir_map
projects_dir_map["flink"]="./projects/apache/flink"
projects_dir_map["kafka"]="./projects/apache/kafka"
projects_dir_map["tomcat"]="./projects/apache/tomcat"
projects_dir_map["storm"]="./projects/apache/storm"
projects_dir_map["hive"]="./projects/apache/hive"


	if [ "$GIT_CLONE_MODE" = "ssh" ]; then
		echo "Cloning in ssh mode..."
		git clone git@github.com:apache/flink.git $flink
	elif [ "$GIT_CLONE_MODE" = "https" ]; then
		echo "Cloning in https mode..."
		git clone https://github.com/apache/flink.git $flink
	else
		echo "Clone mode has to be one of the following: [ssh, https]"
		exit 1
	fi

echo "-------------------[ APACHE FLINK ]-------------------"
python3 -B evaluate.py "java" "$flink" "['java']"
echo "-------------------[ APACHE FLINK ]-------------------"
echo ""

echo "-------------------[ APACHE KAFKA ]-------------------"
python3 -B evaluate.py "java" "$kafka" "['java']"
echo "-------------------[ APACHE KAFKA ]-------------------"
echo ""

echo "-------------------[ APACHE TOMCAT ]-------------------"
python3 -B evaluate.py "java" "$tomcat" "['java']"
echo "-------------------[ APACHE TOMCAT ]-------------------"
echo ""

echo "-------------------[ APACHE STORM ]-------------------"
python3 -B evaluate.py "java" "$storm" "['java']"
echo "-------------------[ APACHE STORM ]-------------------"
echo ""

echo "-------------------[ APACHE HIVE ]-------------------"
python3 -B evaluate.py "java" "$hive" "['java']"
echo "-------------------[ APACHE HIVE ]-------------------"
echo ""
