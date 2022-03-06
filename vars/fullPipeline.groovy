def call(Map c = [:], Closure body = {}) {
  def config = createConfiguration(body)
    
  pipeline {
    parameters {
      string(
        name: 'extraMavenArguments',
        defaultValue: config.extraMavenArguments,
        description: "Extra arguments to be passed to maven (for testing; overrides only current build)")
      string(
        name: 'agentLabel',
        defaultValue: config.agentLabel,
        description: "Eligible agents (in case a build keeps running on a broken agent; overrides only current build)")
    }

    agent {
      label params.agentLabel ?: config.agentLabel
    }
  
    tools {
      maven config.maven
      jdk config.jdk
    }
  
    options {
      buildDiscarder(logRotator(
        numToKeepStr: '25',
        artifactNumToKeepStr: '5'
      ))
      
      // Seems not to be working reliably yet: https://issues.jenkins-ci.org/browse/JENKINS-48556
      // timestamps()
    }
      
    stages {
      // Display information about the build environemnt. This can be useful for debugging
      // build issues.
      stage("Build info") {
        steps {
          echo '=== Environment variables ==='
          script {
            if (isUnix()) {
              sh 'printenv'
            }
            else {
              bat 'set'
            }
          }
        }
      }
          
      // Perform a merge request build. This is a conditional stage executed with the GitLab
      // sources plugin triggers a build for a merge request. To avoid conflicts with other
      // builds, this stage should not deploy artifacts to the Maven repository server and
      // also not install them locally.
      stage("Pull request build") {
        when { branch 'PR-*' }
      
        steps {
          script {
            currentBuild.description = 'Triggered by: <a href="' + CHANGE_URL + '">' + BRANCH_NAME +
              ': ' + env.CHANGE_BRANCH + '</a> (' +  env.CHANGE_AUTHOR_DISPLAY_NAME + ')'
          }
  
          withMaven(maven: config.maven, jdk: config.jdk) {
            script {
              def mavenCommand = 'mvn ' +
                  params.extraMavenArguments +
                  ' -U -Dmaven.test.failure.ignore=true clean verify';
                  
              if (isUnix()) {
                sh script: mavenCommand
              }
              else {
                bat script: mavenCommand
              }
            }
          }
          
          script {
            def mavenConsoleIssues = scanForIssues tool: mavenConsole()
            def javaIssues = scanForIssues tool: java()
            def javaDocIssues = scanForIssues tool: javaDoc()
            publishIssues issues: [mavenConsoleIssues, javaIssues, javaDocIssues]
          }
        }
      }
      
      // Perform a SNAPSHOT build of a main branch. This stage is typically executed after a
      // merge request has been merged. On success, it deploys the generated artifacts to the
      // Maven repository server.
      stage("SNAPSHOT build") {
        when { branch pattern: "main|main-v2", comparator: "REGEXP" }
        
        steps {
          withMaven(maven: config.maven, jdk: config.jdk) {
            script {
              def mavenCommand = 'mvn ' +
                params.extraMavenArguments +
                ' -U -Dmaven.test.failure.ignore=true clean deploy'
                
              if (isUnix()) {
                sh script: mavenCommand
              }
              else {
                bat script: mavenCommand
              }
            }
          }
          
          script {
            def mavenConsoleIssues = scanForIssues tool: mavenConsole()
            def javaIssues = scanForIssues tool: java()
            def javaDocIssues = scanForIssues tool: javaDoc()
            publishIssues issues: [mavenConsoleIssues, javaIssues, javaDocIssues]
          }
        }
      }
    }
  }
}