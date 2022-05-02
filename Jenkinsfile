pipeline {
    agent any
    stages {
        stage ('Build') {
            stages {
                stage ('Linux x64') {
                    stages {
                        stage ('Build') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn clean package -Plinux64"
                                }
                            }
                        }
                        stage ('Test') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn test -Plinux64"
                                }
                            }
                        }
                        stage ('Deploy') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn deploy -Plinux64 -Pcoloniergames-repository"
                                }
                            }
                        }
                    }
                }
                stage ('Windows x64') {
                    stages {
                        stage ('Build') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn clean package -Pwin64"
                                }
                            }
                        }
                        stage ('Test') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn test -Pwin64"
                                }
                            }
                        }
                        stage ('Deploy') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn deploy -Pwin64 -Pcoloniergames-repository"
                                }
                            }
                        }
                    }
                }
                stage ('Windows x86') {
                    stages {
                        stage ('Build') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn clean package -Pwin32"
                                }
                            }
                        }
                        stage ('Test') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn test -Pwin32"
                                }
                            }
                        }
                        stage ('Deploy') {
                            steps {
                                withMaven (
                                    maven: 'Maven',
                                    jdk: 'adopt-11'
                                ) {
                                    sh "mvn deploy -Pwin32 -Pcoloniergames-repository"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}