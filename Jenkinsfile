pipeline {
    agent any
	    tools {
        // Install the Maven version configured as "Maven_3.9.1_tar" and add it to the path.
        maven "Maven_3.9.1_tar"
        jdk "Java17"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/SimonPolonskyi/ta_lab_01_blogwebapp.git'
                sh 'sed -i "s|spring.datasource.url=jdbc:postgresql:.*:5432|spring.datasource.url=jdbc:postgresql://wapp_postgres:5432|g" src/test/resources/application-test.properties'
			//	sh 'echo \'ENTRYPOINT ["mvn","--f","/usr/src/app/pom.xml", "clean", "test"]\' >> Dockerfile'
            }
        }

        stage('Start Postgres') {
            steps {
                script {
                    sh 'docker network create wappnetwork'
                    sh 'docker run -d --network=wappnetwork --name wapp_postgres -e POSTGRES_DB=test_webap_db -e POSTGRES_USER=wapp_test -e POSTGRES_PASSWORD=wapp_test_pass -p 5432:5432 postgres:latest'
                    sh "until docker exec wapp_postgres pg_isready -U wapp_test -d test_webap_db; do echo waiting for postgres; sleep 3; done;"
					sleep 20
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
				        sh "docker build . -t wapp_test_img"
                        sh 'docker run -d --network=wappnetwork --name wapp_test_c -v ${WORKSPACE}:/usr/src/app wapp_test_img'
						sleep 10
                }
            }
        }
    }

    post {
        always {
            script {
                sh 'docker stop wapp_postgres'
                sh 'docker rm wapp_postgres'
                sh 'docker stop myapp_tst'
                sh 'docker rm myapp_tst'
                sh 'docker network rm wappnetwork'
				junit '**/target/surefire-reports/*.xml'
            }
        }
    }
}