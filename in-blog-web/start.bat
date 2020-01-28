@echo off

rem ---------------------------------------------------------------------------
rem start in-blog-boot server
rem ---------------------------------------------------------------------------

set WEB_HOME=%cd%

set IBO_HOME=%WEB_HOME%/../

echo "IBO_HOME:" %IBO_HOME%

cd %IBO_HOME%

rem git pull

mvn package -Dmaven.test.skip=true

mvn install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-common
mvn clean install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-auth
mvn clean install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-core
mvn clean install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-web


for /f "tokens=5" %%i in ('netstat -aon ^| findstr ":8876"') do (
    set pid=%%i
)

taskkill /f /pid %n%

start /max mvn spring-boot:run

for /f "tokens=5" %%i in ('netstat -aon ^| findstr ":8876"') do (
    set pid=%%i
)

echo "start in-blog-web success,pid:" $pid