#!/usr/bin/env bash
IBO_HOME=/Users/cuiyongxu/workspace/ijson/in-blog-boot

cd $IBO_HOME

git pull

mvn install -Dmaven.test.skip=true

cd $IBO_HOME/in-blog-common
mvn clean install -Dmaven.test.skip=true


cd $IBO_HOME/in-blog-remote
mvn clean install -Dmaven.test.skip=true

cd $IBO_HOME/in-blog-auth
mvn clean install -Dmaven.test.skip=true

cd $IBO_HOME/in-blog-core
mvn clean install -Dmaven.test.skip=true

cd $IBO_HOME/in-blog-web

pid=$(ps -ef | grep "in-blog-web" | grep -v grep | awk '{print $2}')

echo "current in-blog-web pid:" $pid

if [ ! -n "$pid" ];then
    echo "service not started"
else
    kill -9 $pid
fi


DATE=$(date +%Y%m%d%H%M%S)
cd $IBO_HOME/in-blog-web

if [ ! -f "$IBO_HOME/in-blog-web/nohup.out" ]; then
    if [ ! -d "$IBO_HOME-log/" ]; then
        mkdir $IBO_HOME-log/
    fi
    mv $IBO_HOME/in-blog-web/nohup.out $IBO_HOME-log/$DATE.log
fi

cd $IBO_HOME/in-blog-web/
nohup mvn spring-boot:run >> nohup.out 2>&1 &

pid=$(ps -ef | grep "in-blog-web" | grep -v grep | awk '{print $2}')
echo "start in-blog-web success, pid:" $pid
