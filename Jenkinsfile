
pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = 'nerender/wms-supplier-service'
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    env.IMAGE_TAG = sh(
                        script: 'git rev-parse --short=7 HEAD',
                        returnStdout: true
                    ).trim()
                }

                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login \
                          -u "$DOCKER_USER" --password-stdin

                        docker push "$IMAGE_NAME:$IMAGE_TAG"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Supplier Service image pushed successfully"
        }

        failure {
            echo "Supplier Service CI pipeline failed"
        }

        always {
            sh 'docker logout || true'
        }
    }
}
