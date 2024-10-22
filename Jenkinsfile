pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker_hub_Id'
        DOCKER_IMAGE_PREFIX = 'alrk/nyam-config-server'
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

                               // config-server 빌드
                               dir('server/config-server') {
                                   sh '../../gradlew clean build'
                               }
                               // eureka-server 빌드
                               dir('server/eureka-server') {
                                   sh '../../gradlew clean build'

                               }

                               // gateway-server 빌드
                               dir('server/gateway-server') {
                                   sh '../../gradlew clean build'

                               }

                               dir('service/admin-service') {
                                   sh '../../gradlew clean build'

                               }

                               dir('service/chat-service') {
                                   sh '../../gradlew clean build'

                               }

                               dir('service/post-service') {
                                   sh '../../gradlew clean build'

                               }

                               dir('service/restaurant-service') {
                                   sh '../../gradlew clean build'

                               }

                               dir('service/user-service') {
                                   sh '../../gradlew clean build'

                               }
                           }
                       }
                   }
        }






        stage('Build Microservices') {
            parallel {
                stage('Build Config Server') {
                    steps {
                        sh 'cd nyamnyam-spring-cloud/server/config-server && ./gradlew build'
                    }
                }
                stage('Build Eureka Server') {
                    steps {
                        sh 'cd nyamnyam-spring-cloud/server/eureka-server && ./gradlew build'
                    }
                }
                stage('Build Gateway') {
                    steps {
                        sh 'cd nyamnyam-spring-cloud/server/gateway-server && ./gradlew build'
                    }
                }
                stage('Build Other Microservices') {
                    steps {
                        sh './gradlew -p nyamnyam-spring-cloud/service/admin-service build'
                        sh './gradlew -p nyamnyam-spring-cloud/service/chat-service build'
                        sh './gradlew -p nyamnyam-spring-cloud/service/post-service build'
                        sh './gradlew -p nyamnyam-spring-cloud/service/restaurant-service build'
                        sh './gradlew -p nyamnyam-spring-cloud/service/user-service build'
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    // Config Server Dockerfile 위치로 이동하여 이미지 빌드
                    sh "cd nyamnyam-spring-cloud/server/config-server && docker build -t alrk/nyam-config-server:latest ."

                    // Docker Compose 파일이 있는 디렉토리로 이동하여 이미지 빌드
                    sh '''
                    cd nyamnyam-spring-cloud
                    docker-compose build
                    '''
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    sh 'docker push ${repository}:latest' // Push the correct repository
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
