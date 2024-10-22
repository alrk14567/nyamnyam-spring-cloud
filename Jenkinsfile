pipeline {
    agent any

    environment {
        repository = "alrk/nyam-config-server"  //docker hub id와 repository 이름
        DOCKERHUB_CREDENTIALS = credentials('docker_hub_Id') // jenkins에 등록해 놓은 docker hub credentials 이름
        dockerImage = "alrk/nyam-config-server"
    }

    stages {
             agent any
                 stages {
                     stage('Clone Spring Cloud repository') {
                         steps {
                             // Spring Cloud 리포지토리 클론
                             git url: 'https://github.com/alrk14567/nyamnyam-spring-cloud.git', branch: 'main'
                         }
                     }

                     stage('Clone Config Server into Spring Cloud directory') {
                         steps {
                             // /server/config-server에 Config Server 리포지토리 클론
                             sh '''
                             cd nyamnyam-spring-cloud/server
                             git clone https://github.com/alrk14567/nyamnyam-config-server.git config-server
                             '''
                         }
                     }
                     stage('Clone Secret Server into Config Server resources') {
                         steps {
                             // /config-server/main/resources/에 Secret Server 리포지토리 클론
                             sh '''
                             cd nyamnyam-spring-cloud/server/config-server/src/main/resources
                             git clone https://github.com/alrk14567/nyamnyam-secret-server.git secret-server
                             '''
                         }
                     }
                 }

            stage('Grant execute permissions') {
                steps {
                    // gradlew 파일에 실행 권한 부여
                    sh 'chmod +x gradlew'
                }
            }

            stages {
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
                        sh 'docker push whdcks420/lunch:3.0'
                    }
                }
            }

            stage('Cleaning up') {
                steps {
                    sh "docker rmi whdcks420/lunch:3.0"
                }
            }
        }
    }
