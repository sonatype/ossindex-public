@Library(['private-pipeline-library', 'jenkins-shared']) _

mavenPipeline(
  mavenVersion: 'Maven 3.6.x',
  javaVersion: 'Java 8',
  usePublicSettingsXmlFile: true,
  useEventSpy: false,
  testResults: [ '**/target/*-reports/*.xml' ]
)
