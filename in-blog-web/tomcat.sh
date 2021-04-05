#!/usr/bin/env bash

# 1. set in-blog-boot dir
# shellcheck disable=SC2046
IBO_HOME=$(dirname $(pwd))

# 2. set tomcat home path
#TOMCAT_HOME=~/software/tomcat-8.5.58
TOMCAT_HOME=~/develop/blog_web

cd $IBO_HOME || exit

#git pull

mvn clean package install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip

cd $IBO_HOME/in-blog-common || exit
mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip

cd $IBO_HOME/in-blog-core || exit
mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip

cd $IBO_HOME/in-blog-web || exit
mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dgpg.skip=true

pid=$(ps -ef | grep "in-blog-web" | grep -v grep | awk '{print $2}')

echo "current in-blog-web pid:" $pid

if [ ! -n "$pid" ];then
    echo "service not started"
else
    kill -9 $pid
fi

rm -fr $TOMCAT_HOME/webapps/ROOT.war
rm -fr $TOMCAT_HOME/webapps/ROOT

cp -fr $IBO_HOME/in-blog-web/target/in-blog-web-1.0.1-SNAPSHOT.war $TOMCAT_HOME/webapps/ROOT.war

cd $TOMCAT_HOME/bin || exit

#sh shutdown.sh

#sh startup.sh

#echo "start tomcat"

echo "done..."
