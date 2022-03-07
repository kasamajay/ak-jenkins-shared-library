def call(Map config = [:], Closure body = {}) {
    tmpCfg = createConfiguration(body)
    
    pipeline {
        agent any
        stages {
            stage {
                steps {
                    echo 'Hello'
                }
            }
        }
    }
}