String jdkVersion = 'Java 8'

String mavenVersion = 'Maven 3.3.x'
String mavenSettings = 'public-settings.xml'
String mavenRepo = '.repo'
String mavenOptions = '-V -B -e'

pipeline {
  agent {
    label 'ubuntu-zion'
  }

  triggers {
    pollSCM('*/15 * * * *')
  }

  tools {
    maven mavenVersion
    jdk jdkVersion
  }

  stages {
    stage('Build') {
      steps {
        withMaven(maven: mavenVersion, jdk: jdkVersion, mavenSettingsConfig: mavenSettings, mavenLocalRepo: mavenRepo) {
          sh "mvn $mavenOptions clean install"
        }
      }
    }
  }

  post {
    always {
      junit '**/target/*-reports/*.xml'
    }
  }
}