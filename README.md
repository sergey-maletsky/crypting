Java version is 1.8.0_131

Deployment

I Common information

1. https server is running on the 3000 port and proxy to 6060 port of the main application
2. Swagger Api Documentation is available at http://localhost:6060/swagger-ui.html

II Configuring database

Database connection settings are in the follow file:
src/main/resources/spring/database.properties

1. Install PostgreSQL 9.6
2. Execute the createdb.sql script within PostgreSQL 
console under superuser(it's postgres in here): 
psql -U postgres -a -f createdb.sql

The script will add "crypting" database as well as 
add new user with name "crypter" with access rights only to this database. 

III Build automation tool
There is Gradle as a build automation tool in this project.

To build and run project you could execute following
command in the terminate or console while you are in 
the root of project:
1. ./gradlew clean build
2. ./gradlew bootJar
3. java -jar build/libs/crypting-1.0.0.jar
4. Go to the frontend directory and execute following:
   1) npm i
   2) bower i
   3) gulp watch-dev
5. Go to https://localhost:3000/
6. To get a test certificate you could visit https://www.cryptopro.ru/sites/default/files/products/cades/demopage/main.html 
and go by a link on "Проверить работу установленного плагина" line 
or go by a link on "Cертификат ключа подписи, который можно получить на странице тестового центра"

IV Using certifying center certificates

All below properties are located in the application.properties file.

To check a chain of certificate you can set update.certifications.job.is.switch.on and app.validate.user.certs.by.ca.strictly properties to true. 
Also you can update certifying center certificates while starting the application by running the certifications job. To do certifications job was available you need to set app.run.ca.update.while.starting to true 
By default it is set to the false value for test aims.

V JCP Installing

There is jcp-2.0.39442.zip archive in the root of the project that you need to unzip anywhere.
1. go to the unziped jcp directory
2. execute "sudo ./setup_console.sh /usr/lib/jvm/java-<version>-oracle" command if you use Linux or "setup_console.bat /usr/lib/jvm/java-<version>-oracle" if you use Windows

