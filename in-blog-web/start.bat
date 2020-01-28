@echo off

rem ---------------------------------------------------------------------------
rem start in-blog-boot server
rem ---------------------------------------------------------------------------

set "IBO_HOME=%cd%"

cd %IBO_HOME%

git pull

mvn install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-common
mvn clean install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-auth
mvn clean install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-core
mvn clean install -Dmaven.test.skip=true

cd %IBO_HOME%/in-blog-web
