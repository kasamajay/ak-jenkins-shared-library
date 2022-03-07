def call() {
    pipeline {
        agent { label any }
        stages {
            stage('checkout') {
                steps {
                    sh "echo 'Hello'"
                }
            }
        }
    }
}