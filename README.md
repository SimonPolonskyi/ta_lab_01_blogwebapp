# TA Lab Blog

This is a Java-based web application. The application provides a web interface for creating, managing, and viewing blog posts, as well as leaving comments on posts.

## Functionality
The application provides the following functionality:
User authentication: users can create an account, log in, and log out of the application.
Password reset: users can reset their password if they forget it.
Account management: users can edit their account information, such as their name, email address, and password.
Create and publish posts: users can write and publish posts on their own blog. 
Leave comments: users can comment on posts, and the comments will be displayed on the post page.
View posts and comments: users can view posts and their associated comments.

## Prerequisites
Before running the application, you must have the following installed:<br />
&nbsp;&nbsp;&nbsp;Java 17+<br />
&nbsp;&nbsp;&nbsp;Maven 3.6 or later<br />
&nbsp;&nbsp;&nbsp;PostgreSQL<br />
&nbsp;&nbsp;&nbsp;Docker
## Technology

The application uses the following technologies:<br />
&nbsp;&nbsp;&nbsp;Java<br />
&nbsp;&nbsp;&nbsp;Spring Boot 3<br />
&nbsp;&nbsp;&nbsp;Freemarker<br />
&nbsp;&nbsp;&nbsp;Bootstrap 5<br />
&nbsp;&nbsp;&nbsp;PostgreSQL<br />
&nbsp;&nbsp;&nbsp;Docker

## Installation and run

### Install on the server
To install and run the application on the server, follow these steps:

- Clone the repository to your local machine.
- Navigate into the docker directory
- Modyfy configure_and_run.sh

```bash
#PORT
LISTEN_PORT=80 

#var for entrypoint.sh
DB_NAME=tablogdb
DB_USER=tablog
DB_USER_PASS=FKLdfskwe9622

#var for docker file
PROD_HOSTNAME=talab.test.net
POSTFIX_HOSTNAME=${PROD_HOSTNAME}
NOTIFICATION_FROM=testapp@talab.test.net
```
Where:<br />
&nbsp;&nbsp;&nbsp;LISTEN_PORT  - Port on which the application will be available.<br />
&nbsp;&nbsp;&nbsp;DB_NAME - The name of the database that will be created to store the data.<br />
&nbsp;&nbsp;&nbsp;DB_USER_PASS - Password for the created database<br />
&nbsp;&nbsp;&nbsp;PROD_HOSTNAME - host name for aplication<br />
&nbsp;&nbsp;&nbsp;NOTIFICATION_FROM - the address on behalf of which notifications will be sent to the user<br />

Run commands:
```bash
chmod +x configure_and_run.sh
./configure_and_run.sh
```
After the script is executed, the application will be available by url: http://PROD_HOSTNAME:LISTEN_PORT

### Build and run in IDE

- Crete new DB in the PostgreSQL
- Create user with superuser privileges
- Open project in IntelliJ IDEA
- Open the src/main/resources/application.properties file and modify parameters 


|Key| Value                                                                                 |
|---|---------------------------------------------------------------------------------------|
|server.port| The port on which the application should run                                          |
|spring.datasource.url| url to created db                                                                     |
|spring.datasource.username| db user name                                                                          |
|spring.datasource.password| db user password                                                                      |
|spring.mail.host| The IP address to which the SMTP server should bind                                   |
|spring.mail.username| The identifier of the sender of the email on SMTP server                              |
|spring.mail.password| The credential that is used to authenticate the email sender on SMTP server           |
|spring.mail.port| The port on which the SMTP server should run                                          |
|spring.mail.protocol| smtp protocol|


Run the  application via context menu for src/main/java/edu/sumdu/blogwebapp/BlogwebappApplication file
Open your web browser and navigate to http://localhost:8080 to access the application.

