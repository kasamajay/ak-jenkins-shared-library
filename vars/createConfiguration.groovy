#!groovy
def call(body) {
    def config = [
        agentLabel: '',
        maven: 'maven_latest',
        jdk: 'jdk_1.8_latest',
        extraMavenArguments: ''
    ]
    
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    return config
}