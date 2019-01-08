
pipeline {
  agent any
  environment {
      CHORE_RELEASE = sh script: "git log -1 | grep 'chore(release):'", returnStatus: true
      CHORE_SNAPSHOT = sh script: "git log -1 | grep 'chore(snapshot):'", returnStatus: true
  }
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
      parallel {
        stage('Deploy To Release Maven') {
          when {
            allOf {
              expression {
                "${CHORE_SNAPSHOT}" == "0" && || currentBuild.result == 'SUCCESS'
              }
            }
          }
          steps {
            sh 'aws s3 cp build/repo/ s3://hrznstudio.com/maven/release/ --recursive --acl public-read'
          }
        }
        stage('Deploy To Snapshot Maven') {
          when {
            allOf {
              expression {
                "${CHORE_SNAPSHOT}" == "0" && || currentBuild.result == 'SUCCESS'
              }
            }
          }
          steps {
            sh 'aws s3 cp build/repo/ s3://hrznstudio.com/maven/snapshot/ --recursive --acl public-read'
          }
        }
      }
    }
    stage('Deploy Release') {
      parallel {
        stage('Deploy To CurseForge') {
          when {
            allOf {
              expression {
                "${CHORE_RELEASE}" == "0" && || currentBuild.result == 'SUCCESS'
              }
            }
          }
          steps {
            sh './gradlew curseforge'
          }
        }
      }
    }
  }
  post {
    always {
      discordSend(description: 'Horizon CI Build', footer: '', link: env.BUILD_URL, result: currentBuild.currentResult, unstable: false, title: JOB_NAME, webhookURL: env.webhookURL)
    }
  }
}