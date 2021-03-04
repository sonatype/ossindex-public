@Library(['private-pipeline-library', 'jenkins-shared']) _

mavenPipeline(
  mavenVersion: 'Maven 3.6.x',
  javaVersion: 'Java 8',
  usePublicSettingsXmlFile: true,
  useEventSpy: false,
  runFeatureBranchPolicyEvaluations: true,
  iqPolicyEvaluation: { stage ->
    nexusPolicyEvaluation iqStage: stage, iqApplication: 'ossindex-public',
      iqScanPatterns: [[scanPattern: 'no-such-path/*']]
  },
  testResults: [ '**/target/*-reports/*.xml' ]
)
