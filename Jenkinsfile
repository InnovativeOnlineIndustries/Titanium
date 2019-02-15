pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        echo "Running build #${env.BUILD_ID} on ${env.JENKINS_URL}"
        checkout scm
      }
    }
    stage('Build') {
      steps {
        sh 'rm -rf build/libs/'
        sh 'rm -rf build/repo/'
        sh 'chmod +x gradlew'
        sh './gradlew build uploadArchives --no-daemon'
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
        archiveArtifacts 'build/libs/*'
      }
    }
    stage('Deploy To Maven') {
      when {
        allOf {
          expression {
            currentBuild.result == 'SUCCESS'
          }

        }

      }
      steps {
        sh 'aws s3 cp build/repo/ s3://hrznstudio.com/maven/ --recursive --acl public-read'
      }
    }
  }
  post {
    always {
      discordSend(description: 'Horizon CI Build', footer: '', link: env.BUILD_URL, result: currentBuild.currentResult, unstable: false, title: JOB_NAME, webhookURL: env.webhookURL)

    }

  }
}