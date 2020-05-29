::common
::remote
::auth
::core
::web
::start

set WEB_HOME=%cd%
set IBO_HOME=%WEB_HOME%/../


@echo off


echo "IBO_HOME:" %IBO_HOME%
cd %IBO_HOME%


rem @echo build package
rem call mvn package -Dmaven.test.skip=true


rem @echo build install
rem call mvn install -Dmaven.test.skip=true


@echo build common
cd %IBO_HOME%/in-blog-common
call mvn install -Dmaven.test.skip=true


@echo build remote
cd %IBO_HOME%/in-blog-remote
call mvn install -Dmaven.test.skip=true


@echo build auth
cd %IBO_HOME%/in-blog-auth
call mvn install -Dmaven.test.skip=true



@echo build core
cd %IBO_HOME%/in-blog-core
call mvn install -Dmaven.test.skip=true


@echo build web
cd %IBO_HOME%/in-blog-web
call mvn install -Dmaven.test.skip=true

@echo build start
call mvn spring-boot:run >> run.log
