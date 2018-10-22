pipeline {
    agent {
        label 'fat'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh "rm -rf build/libs/"
                sh "chmod +x gradlew"
                sh './gradlew build --no-daemon'
            }
        }
        stage('Archive') {
            when {
              allOf {
                  expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                  }
              }
            }
            steps {
                archiveArtifacts artifacts: 'build/libs/*'
            }
        }
        stage('Deploy Maven') {
            when {
              allOf {
                  branch 'release'
                  expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                  }
              }
            }
            steps {
                echo "Maven deployment not ready"
            }
        }
        stage('Deploy CurseForge') {
            when {
              allOf {
                  branch 'release'
                  expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                  }
              }
            }
            steps {
                echo "CurseForge deployment not ready"
            }
        }
    }
}