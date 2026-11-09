
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        DOCKER_IMAGE = 'nerender/wms-supplier-service'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Tools') {
            steps {
                sh '''
                    set -e

                    java -version
                    mvn -version
                    git --version
                    docker version
                    docker buildx version
                '''
            }
        }

        stage('Build and Test') {
            steps {
                sh '''
                    set -e
                    mvn --batch-mode clean package -DskipTests
                '''
            }
        }

        stage('Create Image Tag') {
            steps {
                script {
                    env.SHORT_COMMIT = sh(
                        script: 'git rev-parse --short=7 HEAD',
                        returnStdout: true
                    ).trim()

                    env.FULL_IMAGE =
                        "${env.DOCKER_IMAGE}:${env.SHORT_COMMIT}"

                    echo "Docker image: ${env.FULL_IMAGE}"
                }
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x

                        printf '%s' "${DOCKER_TOKEN}" |
                        docker login \
                          --username "${DOCKER_USERNAME}" \
                          --password-stdin
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    set -e

                    docker buildx build \
                      --load \
                      --tag "${FULL_IMAGE}" \
                      .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                sh '''
                    set -e
                    docker push "${FULL_IMAGE}"
                '''
            }
        }
    }

    post {
        success {
            echo "Supplier Service CI completed successfully."
            echo "Docker image pushed: ${env.FULL_IMAGE}"
        }

        failure {
            echo "Supplier Service CI pipeline failed."
        }

        always {
            sh 'docker logout || true'
            cleanWs()
        }
    }
}
