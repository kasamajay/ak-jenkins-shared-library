def call(Map config = [:], Closure body = {}) {
    
    pipeline {
        agent {
            label any
        }
        // agent {
        //     node {
        //         label any
        //     }
        // }
        // agent {
        //     docker {        
        //     }
        // }
        // agent {
        //     kubernetes {        
        //     }
        // }
        triggers {}
        options {}
        parameters {}
        tools {}
        environment {}
        stages {
            stage('checkout') {
                options {}
                when {}
                input {}
                steps {
                    sh 'echo "checkout code"'
                    echo 'checkout code'
                    script {
                        println("checkout code")
                    }
                }
            }
            post {
            }

            stage('build') {
                steps {
                    sh 'echo "build"'
                }
            }
            stage('package') {
                steps {
                    sh 'echo "package"'
                }
            }
        }
        post {
        }
    }
    body()
}