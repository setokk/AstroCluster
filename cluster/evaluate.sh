#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo "Usage: $0 \"<git_clone_mode: str>\""
	echo "Examples:"
	echo "        $0 \"ssh\""
	echo "        $0 \"https\""
	exit 1
fi

flink="./projects/apache/flink"
kafka="./projects/apache/kafka"
tomcat="./projects/apache/tomcat"
storm="./projects/apache/storm"
hive="./projects/apache/hive"
if [ ! -d "$flink"  ] ||
   [ ! -d "$kafka" ] ||
   [ ! -d "$tomcat" ] ||
   [ ! -d "$storm" ] ||
   [ ! -d "$hive" ]; then
	
	rm -rf "$flink"
	rm -rf "$kafka"
	rm -rf "$tomcat"
	rm -rf "$storm"
	rm -rf "$hive" 
	 
	git_clone_mode=$1
	if [ "$git_clone_mode" = "ssh" ]; then
		echo "Cloning in ssh mode..."
		git clone git@github.com:apache/flink.git "$flink"
		git clone git@github.com:apache/kafka.git "$kafka"
		git clone git@github.com:apache/tomcat.git "$tomcat"
		git clone git@github.com:apache/storm.git "$storm"
		git clone git@github.com:apache/hive.git "$hive"
	elif [ "$git_clone_mode" = "https" ]; then
		echo "Cloning in https mode..."
		git clone https://github.com/apache/flink.git $flink
		git clone https://github.com/apache/kafka.git $kafka
		git clone https://github.com/apache/tomcat.git $tomcat
		git clone https://github.com/apache/storm.git $storm
		git clone https://github.com/apache/hive.git $hive
	else
		echo "Clone mode has to be one of the following: [ssh, https]"
		exit 1
	fi
fi

python3 -B evaluate.py "java" "$flink" "['java']"
python3 -B evaluate.py "java" "$kafka" "['java']"
python3 -B evaluate.py "java" "$tomcat" "['java']"
python3 -B evaluate.py "java" "$storm" "['java']"
python3 -B evaluate.py "java" "$hive" "['java']"
