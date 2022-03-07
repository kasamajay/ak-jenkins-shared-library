def call(Map config = [:], Closure body = {}) {
    tmpCfg = createConfiguration(body)

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