pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'alrk'
        DOCKER_IMAGE_PREFIX = 'alrk/nyam-config-server'
         services = "server/config-server,server/eureka-server,server/gateway-server,service/admin-service,service/chat-service,service/post-service,service/restaurant-service,service/user-service"
         DOCKERHUB_CREDENTIALS = credentials('docker_hub_Id')
    }

    stages {
            stage('Checkout SCM') {
                steps {
                    script {
                        dir('nyamnyam.kr') {
                            checkout scm
                        }
                    }
                }
            }
             stage('Git Clone') {
                        steps {
                            script {
                            sh 'pwd'

                                dir('nyamnyam.kr/server/config-server') {
                                    git branch: 'main', url: 'https://github.com/alrk14567/nyamnyam-config-server.git', credentialsId: 'github_nyamnyam_access_token'
                                }

                                dir ('nyamnyam.kr/server/config-server/src/main/resources/secret-server') {
                                    git branch: 'main', url: 'https://github.com/alrk14567/nyamnyam-secret-server.git', credentialsId: 'github_nyamnyam_access_token'

                                }
                            }
                        }
             }



        stage('Build JAR') {
                   steps {
                       script {
                           // 각 서버에 대해 gradlew를 실행
                           dir('nyamnyam.kr') {
                               sh 'chmod +x gradlew' // gradlew에 실행 권한 부여

                               // services 환경 변수를 Groovy 리스트로 변환
                               def servicesList = env.services.split(',')


                               servicesList.each { service ->
                                   dir(service) {
                                       // ./gradlew clean bootJar 명령어 실행
                                       sh "../../gradlew clean bootJar"
                                   }
                               }
                           }
                       }

                   }
        }

        stage('Build Docker Images') {
                    steps {
                        script {
                            dir('nyamnyam.kr') {
                                sh "cd server/config-server && docker build -t ${DOCKER_CREDENTIALS_ID}/nyamnyam-config-server:latest ."
                            }

                            dir('nyamnyam.kr') {
                                sh "docker-compose build"
                            }
                        }
                    }
                }

          stage('Login to Docker Hub') {
                            steps {
                                sh '''
                                echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                                '''
                            }
                 }


            stage('Docker Push') {
                        steps {
                            script {

                                def servicesList = env.services.split(',')

                                servicesList.each { service ->
                                    def serviceName = service.split('/')[1] // 서비스 이름 추출
                                    // 각 서비스의 Docker 이미지를 푸시
                                    sh "docker push ${DOCKER_CREDENTIALS_ID}/nyamnyam-${serviceName}:latest"
                                }
                            }
                        }
                    }


        stage('Cleaning up') {
            steps {
                sh "docker rmi ${repository}:latest" // Clean up the pushed image
            }
        }
    }
}
