#!/usr/bin/env bash

# shellcheck disable=SC2046
IBO_HOME=$(dirname $(pwd))

cd "$IBO_HOME"

git pull

mvn install -Dmaven.test.skip=true

# shellcheck disable=SC2164
cd $IBO_HOME/in-blog-common
mvn clean install -Dmaven.test.skip=true

# shellcheck disable=SC2164
cd $IBO_HOME/in-blog-core
mvn clean install -Dmaven.test.skip=true

# shellcheck disable=SC2164
cd $IBO_HOME/in-blog-web

pid=$(ps -ef | grep "in-blog-web" | grep -v grep | awk '{print $2}')

echo "current in-blog-web pid:" $pid

if [ ! -n "$pid" ];then
    echo "service not started"
else
    kill -9 $pid
fi


DATE=$(date +%Y%m%d%H%M%S)
# shellcheck disable=SC2164
cd $IBO_HOME/in-blog-web

if [ ! -f "$IBO_HOME/in-blog-web/run.log" ]; then
    if [ ! -d "$IBO_HOME-log/" ]; then
        mkdir $IBO_HOME-log/
    fi
    mv $IBO_HOME/in-blog-web/run.log $IBO_HOME-log/$DATE.log
fi

# shellcheck disable=SC2164
cd $IBO_HOME/in-blog-web/
nohup mvn spring-boot:run >> run.log 2>&1 &

pid=$(ps -ef | grep "in-blog-web" | grep -v grep | awk '{print $2}')
echo "start in-blog-web success, pid:" $pid
