#!/bin/bash
set -e
DB_NAME=
DB_USER=
DB_USER_PASS=


# Start PostgreSQL
/etc/init.d/postgresql start


# Create user if they don't exist
sudo -u postgres psql -tAc "SELECT 1 FROM pg_roles WHERE rolname='$DB_USER'" | grep -q 1 || (sudo -u postgres createuser --superuser --no-createdb --no-createrole --login $DB_USER && sudo -u postgres psql -c "ALTER USER $DB_USER WITH ENCRYPTED PASSWORD '$DB_USER_PASS';")

# Grant the 'myapp' user permission to create databases
sudo -u postgres psql -c "ALTER USER $DB_USER CREATEDB;"

# Create the database if they don't exist
sudo -u postgres psql -c "SELECT 1 FROM pg_database WHERE datname='$DB_NAME'" | grep -q 1 || sudo -u postgres createdb -O $DB_USER $DB_NAME


# Start Postfix
postfix start

# Start nginx
service nginx start

service postfix restart

# Start the Spring Boot application
java -jar /opt/tablogapp/target/blogwebapp-0.0.1-SNAPSHOT.jar >> /opt/tablogapp/data/logs/logs.log

