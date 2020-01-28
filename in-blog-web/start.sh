#!/usr/bin/env bash
bash_path=/Users/cuiyongxu/workspace/ijson/in-blog-boot

cd $bash_path

#git pull

mvn install -Dmaven.test.skip=true

cd $bash_path/in-blog-common
mvn clean install -Dmaven.test.skip=true

cd $bash_path/in-blog-auth
mvn clean install -Dmaven.test.skip=true

cd $bash_path/in-blog-core
mvn clean install -Dmaven.test.skip=true

cd $bash_path/in-blog-web

pid=$(ps -ef | grep "in-blog-web" | grep -v grep | awk '{print $2}')

echo "current in-blog-web pid:" $pid

if [ ! -n "$pid" ];then
    echo "service not started"
else
    kill -9 $pid
fi


DATE=$(date +%Y%m%d%H%M%S)
cd $bash_path/in-blog-web

if [ ! -f "$bash_path/in-blog-web/nohup.out" ]; then
    if [ ! -d "$bash_path-log/" ]; then
        mkdir $bash_path-log/
    fi
    mv $bash_path/in-blog-web/nohup.out $bash_path-log/$DATE.log
fi

cd $bash_path/in-blog-web/
nohup mvn spring-boot:run >> nohup.out 2>&1 &

pid=$(ps -ef | grep "in-blog-web" | grep -v grep | awk '{print $2}')
echo "start in-blog-web success, pid:" $pid
