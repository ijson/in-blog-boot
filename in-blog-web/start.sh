#!/usr/bin/env bash
bash_path=/home/liyaping/in-blog-boot/

cd $bash_path

git pull

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

kill -9 $pid

DATE=$(date +%Y%m%d%H%M%S)
cd $bash_path/in-blog-web
mv $bash_path/in-blog-web/nohup.out $bash_path-log/$DATE.log

cd $bash_path/in-blog-web/
nohup mvn spring-boot:run >> nohup.out 2>&1 &