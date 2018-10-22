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
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
            }
            steps {
                archiveArtifacts allowEmptyArchive: true, artifacts: 'build/libs/*', onlyIfSuccessful: true
            }
        }
        stage('Deploy Maven') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
            }
            steps {
                echo "Maven deployment not ready"
            }
        }
        stage('Deploy CurseForge') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
            }
            steps {
                echo "CurseForge deployment not ready"
            }
        }
    }
}