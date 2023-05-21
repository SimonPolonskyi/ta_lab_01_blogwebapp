FROM ubuntu:20.04 AS build

# Update the package list and install necessary packages
RUN apt-get update && \
    apt-get install -y maven openjdk-17-jdk openjdk-17-jre wget dos2unix

# Set the JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

#maven for  Java17
WORKDIR /tmp
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.tar.gz && \
    tar xvf apache-maven-3.9.1-bin.tar.gz && \
    rm -rf /usr/share/maven/* && \
    cp -r /tmp/apache-maven-3.9.1/* /usr/share/maven

# Add maven in the PATH environment variable
ENV PATH=$PATH:/usr/share/maven/bin
WORKDIR /usr/src/app/

ENTRYPOINT ["mvn","--f","/usr/src/app/pom.xml", "clean", "test"]
