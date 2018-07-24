Версия Java: 1.8.0_131

Инструкция по развертыванию:

I Общая информация

1. https сервер работает на 3000 порту и проксирует запросы на 6060 порт основного приложения
2. Swagger Api документация доступна по адресу http://localhost:6060/swagger-ui.html

II Подготовка базы данных

Настройки подключения базы данных находятся в файле
src/main/resources/spring/database.properties

1. Установить PostgreSQL 9.6
2. Выполнить createdb.sql скрипт(в корне проекта) из PostgreSQL 
консоли под супер пользователем: 
psql -U postgres -a -f createdb.sql

Скрипт создаст базу данных "crypting" и нового пользователя "crypter" с правами для доступа к этой бд. 

III Инструменты для сборки проекта
В проекте используются Gradle(бекенд) и Gulp(фронтенд) сборщики проектов.

Для того, чтобы собрать и развернуть проект, необходимо выполнить 
следующие команды:
1. перейти в корень проекта
2. выполнить из терминала ./gradlew clean build(Linux) или gradlew.bat clean build(Win)
3. и затем выполнить java -jar build/libs/crypting-1.0.0.jar
4. перейти в директорию frontend/ и выполнить следующие команды:
   1) npm i
   2) bower i
   3) gulp watch-dev
5. в бразере открыть https://localhost:3000/
6. На странице приложения есть ссылка Как войти по ЭП, 
перейдя по которой можно прочесть описание того, как получить тестовые сертификаты 
и какие настройки в системе необходимо осуществить, чтобы с ними работать.  

Для того, чтобы получить тестовые сертификаты, перейдите по ссылке https://www.cryptopro.ru/sites/default/files/products/cades/demopage/main.html и далее перейдите по ссылке 
"Проверить работу установленного плагина" или по ссылке "Cертификат ключа подписи, который можно получить на странице тестового центра".

IV Использование сертификационного центра для проверки валидности пользовательских сертификатов

Для чего это надо? Проводить полные проверки по валидности сертификатов: отозван ли сертификат, находится ли в списке доверенных и др.
В тестовых целях эта функция отключена, т.к. для получения или обновления данных по сертификатам
из авторизованного центра сертификации потребуется не менее часа.

Все свойства, описанные ниже находятся в application.properties

Для включения функции первоначальной загрузки данных и доверенного сертификационного центра, необходимо установить свойство app.run.ca.update.while.starting в значение true

Для того, чтобы включить возможность проверки пользовательских сертификатов, используя сертификационный центр следующие свойства необходимо установить в значение true: 
1. update.certifications.job.is.switch.on 
2. app.validate.user.certs.by.ca.strictly

V Установка JCP

В корне проекта находится файл jcp-2.0.39442.zip archive in the root of the project that you need to unzip anywhere.
1. распаковать архив jcp-2.0.39442.zip, который находится в корне проекта в любую директорию в системе
2. перейти в распакованную директорию
3. выполнить "sudo ./setup_console.sh <path-to-jdk>"(Linux) 
или "setup_console.bat <path-to-jdk>"(Win)
4. следовать указаниям утилиты по установке(все выбирать по умолчанию)  