#PORT
LISTEN_PORT=80

#var for entrypoint.sh
DB_NAME=tablogdb
DB_USER=tablog
DB_USER_PASS=FKLdfskwe9622

#var for docker file
PROD_HOSTNAME=talab.test.net
POSTFIX_HOSTNAME=${POSTFIX_HOSTNAME}
NOTIFICATION_FROM=testapp@talab.test.net


#docker image name and docker container name
IMAGE_NAME=my-ta-blogwebapp-app
CONTAINER_NAME=my-blogapp

#configuration for  entrypoint.sh
sed -i "s|DB_NAME=.*|DB_NAME=$DB_NAME|g" entrypoint.sh
sed -i "s|DB_USER=.*|DB_USER=$DB_USER|g" entrypoint.sh
sed -i "s|DB_USER_PASS=.*|DB_USER_PASS=$DB_USER_PASS|g" entrypoint.sh

#configuration for Dockerfile
sed -i "s|POSTFIX_HOSTNAME=.*|POSTFIX_HOSTNAME=$POSTFIX_HOSTNAME|g" Dockerfile
sed -i "s|DB_URL=.*|DB_URL=jdbc:postgresql://localhost:5432/$DB_NAME|g" Dockerfile
sed -i "s|DB_USER_NAME=.*|DB_USER_NAME=$DB_USER|g" Dockerfile
sed -i "s|DB_PASS=.*|DB_PASS=$DB_USER_PASS|g" Dockerfile
sed -i "s|NOTIFICATION_FROM=.*|NOTIFICATION_FROM=$NOTIFICATION_FROM|g" Dockerfile
sed -i "s|PROD_HOSTNAME=.*|PROD_HOSTNAME=$PROD_HOSTNAME|g" Dockerfile
sed -i "s|EXPOSE *\([0-9]\)*|EXPOSE $LISTEN_PORT|g" Dockerfile

#configuration for springboot_nginx.conf
sed -i "s| *listen *\([0-9]\)*;|listen $LISTEN_PORT;|g" springboot_nginx.conf

#Directory preparation
data_dir=$(pwd)/data/
mkdir -p  ${data_dir}/logs
mkdir -p  ${data_dir}/picture


#docker part
#deletion  image
#docker rmi  my-ta-blogwebapp-app

#deletion container
echo -e "\nStop running Docker containers ${CONTAINER_NAME}, and remove them...n"
docker stop $(docker ps -a | grep ${CONTAINER_NAME} | awk '{print $1}')
docker rm $(docker ps -a | grep ${CONTAINER_NAME} | awk '{print $1}')


#deletion old volumes for full reinstall with data size
#docker volume prune -f

#build image
echo -e "\nDocker build image with name ${IMAGE_NAME} \n"
docker build -t $IMAGE_NAME .

#Start container
echo -e "\nStart Docker container of the image ${IMAGE_NAME} with name ${CONTAINER_NAME} \n"
docker run -d --name $CONTAINER_NAME -p LISTEN_PORT:LISTEN_PORT -v postgres-data:/var/lib/postgresql -v ${data_dir}:/opt/tablogapp/data  $IMAGE_NAME



