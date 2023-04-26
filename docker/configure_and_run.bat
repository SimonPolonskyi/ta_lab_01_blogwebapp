@echo off

:: PORT
set LISTEN_PORT=80

:: var for entrypoint.sh
set DB_NAME=tablogdb
set DB_USER=tablog
set DB_USER_PASS=FKLdfskwe9622

:: var for docker file
set PROD_HOSTNAME=talab.test.net
set POSTFIX_HOSTNAME=%PROD_HOSTNAME%
set NOTIFICATION_FROM=testapp@talab.test.net


:: docker image name and docker container name
set IMAGE_NAME=my-ta-blogwebapp-app
set CONTAINER_NAME=my-blogapp



:: configuration for  entrypoint.sh
powershell -Command "(Get-Content entrypoint.sh) -replace 'DB_NAME=.*', 'DB_NAME=%DB_NAME%' | Set-Content entrypoint.sh"
powershell -Command "(Get-Content entrypoint.sh) -replace 'DB_USER=.*', 'DB_USER=%DB_USER%' | Set-Content entrypoint.sh"
powershell -Command "(Get-Content entrypoint.sh) -replace 'DB_USER_PASS=.*', 'DB_USER_PASS=%DB_USER_PASS%' | Set-Content entrypoint.sh"

:: configuration for Dockerfile
powershell -Command "(Get-Content Dockerfile) -replace 'POSTFIX_HOSTNAME=.*', 'POSTFIX_HOSTNAME=%POSTFIX_HOSTNAME%' | Set-Content Dockerfile"
powershell -Command "(Get-Content Dockerfile) -replace 'DB_URL=.*', 'DB_URL=jdbc:postgresql://localhost:5432/%DB_NAME%' | Set-Content Dockerfile"
powershell -Command "(Get-Content Dockerfile) -replace 'DB_USER_NAME=.*', 'DB_USER_NAME=%DB_USER%' | Set-Content Dockerfile"
powershell -Command "(Get-Content Dockerfile) -replace 'DB_PASS=.*', 'DB_PASS=%DB_USER_PASS%' | Set-Content Dockerfile"
powershell -Command "(Get-Content Dockerfile) -replace 'NOTIFICATION_FROM=.*', 'NOTIFICATION_FROM=%NOTIFICATION_FROM%' | Set-Content Dockerfile"
powershell -Command "(Get-Content Dockerfile) -replace 'PROD_HOSTNAME=.*', 'PROD_HOSTNAME=%PROD_HOSTNAME%' | Set-Content Dockerfile"
powershell -Command "(Get-Content Dockerfile) -replace 'EXPOSE *([0-9])*', 'EXPOSE %LISTEN_PORT%' | Set-Content Dockerfile"

:: configuration for springboot_nginx.conf
powershell -Command "(Get-Content springboot_nginx.conf) -replace ' *listen *([0-9])*;', 'listen %LISTEN_PORT%;' | Set-Content springboot_nginx.conf"




:: Directory preparation
set cur_dir=%cd%
set data_dir=%cd%\data\
mkdir %data_dir%\logs
mkdir %data_dir%\picture

:: docker part
:: deletion  image
:: docker rmi  my-ta-blogwebapp-app

:: deletion container
echo Stop running Docker containers %CONTAINER_NAME%, and remove them...
FOR /f "tokens=*" %%i IN ('docker ps -a -q --filter "name=%CONTAINER_NAME%"') DO docker stop %%i
FOR /f "tokens=*" %%i IN ('docker ps -a -q --filter "name=%CONTAINER_NAME%"') DO docker rm %%i


:: deletion old volumes for full reinstall with data size
:: docker volume prune -f

echo %data_dir%
echo %cd%
echo %cur_dir%

:: build image
echo Docker build image with name %IMAGE_NAME%
docker build -t %IMAGE_NAME% .


:: Start container
echo Start Docker container of the image %IMAGE_NAME% with name %CONTAINER_NAME%
docker run -d --name %CONTAINER_NAME% -p %LISTEN_PORT%:%LISTEN_PORT% -v postgres-data:/var/lib/postgresql -v %data_dir%:/opt/tablogapp/data  %IMAGE_NAME%


