pipeline {
    agent any

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
                sh 'gradlew build'
                sh "chmod +x gradlew"
            }
        }
        stage('Archive') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
            }
            steps {
                archiveArtifacts allowEmptyArchive: true, artifacts: 'build/libs/*.jar', onlyIfSuccessful: true
            }
        }
        stage('Deploy Maven') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
            }
            steps {
                sh 'gradlew publish'
            }
        }
        stage('Deploy CurseForge') {
            when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
            }
            steps {
                sh 'gradlew curseTools'
            }
        }
    }
}