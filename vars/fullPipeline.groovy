def call(Map c = [:], Closure body = {}) {
  def config = createConfiguration(body)
    
    pipeline {
        agent { label any }
        stages {
            stage('checkout') {
                sh "echo 'Hello'"
            }
        }
    }
}