pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'raichu8503/danggn-was'
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        SSH_CREDENTIALS = 'local-ssh' // 추가한 SSH credentials ID
    }

    stages {
        stage('Checkout') {
                steps {
                     git branch: 'main', url: 'git-url'
                }
        }

        stage('Add Env') {
            steps {
                dir('/var/lib/jenkins/workspace/danggn-local') {
                    withCredentials([file(credentialsId: 'danggn-yml', variable: 'DANGGN_YML')]) {
                        sh 'cp ${DANGGN_YML} src/main/resources/application.yml'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                    sh 'chmod +x ./gradlew'
                    sh './gradlew clean build '
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker rmi -f ${DOCKER_IMAGE} || true"
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Login') {
            steps {
                   sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    sh 'docker push ${DOCKER_IMAGE}'
                }
            }
        }

        stage('Deploy to Local Server') {
            steps {
                script {
                    // SSH Credentials을 사용하여 원격 서버에 접속 후 Docker 컨테이너 관리 명령 실행
                    withCredentials([sshUserPrivateKey(credentialsId: env.SSH_CREDENTIALS, keyFileVariable: 'SSH_KEY_FILE')]) {
                        sh """
                        ssh -o StrictHostKeyChecking=no -i "${SSH_KEY_FILE}" root@IP '
                            docker stop danggn-was || true &&
                            docker rm danggn-was || true &&
                            docker rmi ${DOCKER_IMAGE} || true &&
                            docker pull ${DOCKER_IMAGE} &&
                            docker run -d -p 9090:8080 --network host --name danggn-was ${DOCKER_IMAGE}
                        '
                        """

                        sh """
                        ssh -o StrictHostKeyChecking=no -i "${SSH_KEY_FILE}" root@IP '
                            docker stop danggn-was || true &&
                            docker rm danggn-was || true &&
                            docker rmi ${DOCKER_IMAGE} || true &&
                            docker pull ${DOCKER_IMAGE} &&
                            docker run -d -p 8080:8080 --network host --name danggn-was ${DOCKER_IMAGE}
                        '
                        """
                    }
                }
            }
        }
   }
}