IBO_HOME=/Users/cuiyongxu/workspace/ijson/in-blog-boot

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
