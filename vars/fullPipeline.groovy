#!groovy
def call(Map config = [:], Closure body = {}) {
    def tmpCfg = createConfiguration(body)

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

        stages {
            stage('checkout') {
                
                
                
                steps {
                    sh 'echo "checkout code"'
                    echo 'checkout code'
                    script {
                        println("checkout code")
                    }
                }

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
    }
}