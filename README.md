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
Thereis Gradle as a build automation tool in this project.

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