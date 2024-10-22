pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker_hub_Id'
        DOCKER_IMAGE_PREFIX = 'alrk/nyam-config-server'
         services = "server/config-server,server/eureka-server,server/gateway-server,service/admin-service,service/chat-service,service/post-service,service/restaurant-service,service/user-service"
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
                        sh "cd server/config-server && docker build -t nyamnyam/config-server:latest ."
                    }

                    dir('nyamnyam.kr') {
                        sh "docker-compose build"
                    }
                }
            }
        }


        stages {
                stage('Docker Login') {
                    steps {
                        script {
                            // Jenkins Credentials에서 ID를 사용하여 로그인
                            withCredentials([usernamePassword(credentialsId: 'docker_hub_Id', usernameVariable: 'cdutik@gmail.com', passwordVariable: 'gkdl0547!23')]) {
                                sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                            }
                        }
                    }
                }
                stage('Docker Push') {
                    steps {
                        script {
                            sh 'docker tag nyamnyam/config-server alrk/nyamnyam-config-server:latest'
                            sh 'docker push alrk/nyamnyam-config-server:latest'

                            sh 'docker tag nyamnyam/eureka alrk/nyamnyam-eureka-server:latest'
                            sh 'docker push alrk/nyamnyam-eureka-server:latest'

                            sh 'docker tag nyamnyam/gateway-server alrk/nyamnyam-gateway-server:latest'
                            sh 'docker push alrk/nyamnyam-gateway-server:latest'

                            sh 'docker tag nyamnyam/admin-service alrk/nyamnyam-admin-service:latest'
                            sh 'docker push alrk/nyamnyam-admin-service:latest'

                            sh 'docker tag nyamnyam/chat-service alrk/nyamnyam-chat-service:latest'
                            sh 'docker push alrk/nyamnyam-chat-service:latest'

                            sh 'docker tag nyamnyam/post-service alrk/nyamnyam-post-service:latest'
                            sh 'docker push alrk/nyamnyam-post-service:latest'

                            sh 'docker tag nyamnyam/restaurant-service alrk/nyamnyam-restaurant-service:latest'
                            sh 'docker push alrk/nyamnyam-restaurant-service:latest'

                            sh 'docker tag nyamnyam/user-service alrk/nyamnyam-user-service:latest'
                            sh 'docker push alrk/nyamnyam-user-service:latest'
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
