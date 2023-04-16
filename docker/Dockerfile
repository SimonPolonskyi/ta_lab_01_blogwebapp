# Use an official Ubuntu base image
FROM ubuntu:20.04

# Set environment variables for non-interactive installation
ENV DEBIAN_FRONTEND=noninteractive

# Update the package list and install necessary packages
RUN apt-get update && \
    apt-get install -y postfix mailutils postgresql postgresql-contrib git maven openjdk-17-jdk openjdk-17-jre nginx wget sudo

#Fix for maven and Java17
WORKDIR /tmp
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.tar.gz
RUN tar xvf apache-maven-3.9.1-bin.tar.gz
RUN rm -rf /usr/share/maven/*
RUN cp -r /tmp/apache-maven-3.9.1/* /usr/share/maven

#RUN java -version
#RUN mvn -version


# Set up Postfix
ARG POSTFIX_HOSTNAME=
RUN sed -i "s/myhostname.*=.*/myhostname = $POSTFIX_HOSTNAME/g" /etc/postfix/main.cf && \
    sed -i 's/inet_interfaces.*=.*/inet_interfaces = loopback-only/g' /etc/postfix/main.cf && \
    sed -i 's/mydestination.*/mydestination = localhost.$mydomain, localhost, $myhostname/g' /etc/postfix/main.cf



# Clone the application from GitHub and install dependencies
ARG GITHUB_REPO_URL=https://github.com/SimonPolonskyi/ta_lab_01_blogwebapp
RUN git clone $GITHUB_REPO_URL /opt/tablogapp
WORKDIR /opt/tablogapp
RUN mkdir data

# Update the application.properties file for the PostgreSQL connection
ARG DB_URL=
ARG DB_USER_NAME=
ARG DB_PASS=
ARG NOTIFICATION_FROM=
ARG PROD_HOSTNAME=

RUN sed -i "s|spring\.datasource\.url.*=.*|spring.datasource.url=$DB_URL|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|spring\.datasource\.username.*=.*|spring.datasource.username=$DB_USER_NAME|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|spring\.datasource\.password.*=.*|spring.datasource.password=$DB_PASS|g" /opt/tablogapp/src/main/resources/application.properties

RUN sed -i "s|spring\.mail\.host.*=.*|spring.mail.host=localhost|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|spring\.mail\.port.*=.*|spring.mail.port=25|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|spring\.mail\.auth.*=.*|spring.mail.auth=false|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|spring\.mail\.username.*=.*|spring.mail.username=$NOTIFICATION_FROM|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|spring\.mail\.password.*=.*|spring.mail.password=|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|spring\.mail\.protocol.*=.*|spring.mail.protocol=smtp|g" /opt/tablogapp/src/main/resources/application.properties && \
    sed -i "s|server\.prodhost.*=.*|server.prodhost=$PROD_HOSTNAME|g" /opt/tablogapp/src/main/resources/application.properties


RUN sed -i "s|upload\.path.*=.*|upload.path=/opt/tablogapp/data/picture|g" /opt/tablogapp/src/main/resources/application.properties

# Build the Spring Boot application with Maven
RUN mvn package -DskipTests


# Copy the entrypoint script into the container and set permissions
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

#Copy the springboot_nginx.conf file and replace the default NGINX configuration
COPY springboot_nginx.conf /etc/nginx/sites-available/springboot_nginx.conf
RUN ln -s /etc/nginx/sites-available/springboot_nginx.conf /etc/nginx/sites-enabled/
RUN rm /etc/nginx/sites-enabled/default

# Expose the necessary ports
EXPOSE 80

# Run the entrypoint script when the container starts
CMD ["/entrypoint.sh"]
#CMD /bin/bash
