def call() {
    

    pipeline {
        agent any
        stages {
            stage('test') {
                steps {
                    echo 'Hello'
                    script {
                        test1()
                    }
                }
            }
        }
    }
}

def test1() {
    echo 'test1'
    test2()
}

def test2() {
    echo 'test2'
}