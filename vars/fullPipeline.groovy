def call() {
    

    pipeline {
        agent any
        stages {
            stage('test') {
                steps {
                    echo 'Hello'
                }
            }
        }
    }
}