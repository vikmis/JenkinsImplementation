pipeline {
    agent any

    tools {
        maven 'Maven-3.9.9'
    }

    parameters {
        choice(
            name: 'ENV',
            choices: ['dev', 'staging', 'prod'],
            description: 'Target environment'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser to run tests on'
        )
        choice(
            name: 'SUITE',
            choices: ['smoke', 'regression'],
            description: 'TestNG suite to execute'
        )
        booleanParam(
            name: 'FORCE_FAILURE',
            defaultValue: false,
            description: 'Intentionally fail a test'
        )
    }

    environment {
        PROJECT_NAME = 'jenkins-testng-practice'
        TEST_ENV     = "${params.ENV}"
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Print Build Info') {
            steps {
                echo '============================================'
                echo "Job Name    : ${env.JOB_NAME}"
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Workspace   : ${env.WORKSPACE}"
                echo "ENV         : ${params.ENV}"
                echo "BROWSER     : ${params.BROWSER}"
                echo "SUITE       : ${params.SUITE}"
                echo '============================================'
            }
        }

        stage('Checkout') {
            steps {
                echo '[Checkout] Code checked out from GitHub'
                echo "[Checkout] Workspace: ${env.WORKSPACE}"
            }
        }

        stage('Build') {
            steps {
                echo '[Build] Compiling the project...'
                bat 'mvn clean compile -f "C:\\Users\\Vikash Mishra\\eclipse-workspace\\JenkinsDeclarativePipelineWithTestNG\\pom.xml" -q'
                echo '[Build] Compilation successful'
            }
        }

        stage('Run Tests - Parallel') {
            parallel {
                stage('Smoke Tests') {
                    steps {
                        echo '[Smoke] Starting smoke test run...'
                        bat """
                            mvn test ^
                            -f "C:\\Users\\Vikash Mishra\\eclipse-workspace\\JenkinsDeclarativePipelineWithTestNG\\pom.xml" ^
                            -Dsuite=smoke ^
                            -Denv=${params.ENV} ^
                            -Dbrowser=${params.BROWSER} ^
                            -DforceFailure=false
                        """
                        echo '[Smoke] Smoke tests completed'
                    }
                }
                stage('Regression Tests') {
                    steps {
                        echo '[Regression] Starting regression test run...'
                        bat """
                            mvn test ^
                            -f "C:\\Users\\Vikash Mishra\\eclipse-workspace\\JenkinsDeclarativePipelineWithTestNG\\pom.xml" ^
                            -Dsuite=regression ^
                            -Denv=${params.ENV} ^
                            -Dbrowser=${params.BROWSER} ^
                            -DforceFailure=${params.FORCE_FAILURE}
                        """
                        echo '[Regression] Regression tests completed'
                    }
                }
            }
        }

        stage('Publish Reports') {
            steps {
                echo '[Reports] Publishing TestNG test results...'
                step([$class: 'Publisher',
                      reportFilenamePattern: 'C:/Users/Vikash Mishra/eclipse-workspace/JenkinsDeclarativePipelineWithTestNG/target/surefire-reports/testng-results.xml'
                ])
            }
        }
    }

    post {
        always {
            echo "Build #${env.BUILD_NUMBER} completed"
            echo "Result: ${currentBuild.result ?: 'SUCCESS'}"
            junit testResults: 'C:/Users/Vikash Mishra/eclipse-workspace/JenkinsDeclarativePipelineWithTestNG/target/surefire-reports/junitreports/*.xml',
                  allowEmptyResults: true
        }
        success {
            echo '✅ All stages passed!'
        }
        unstable {
            echo '⚠️ Build is UNSTABLE — some tests failed.'
        }
        failure {
            echo '❌ Build FAILED!'
        }
        changed {
            echo "Result changed from last run."
        }
    }
}
```

Save → then run these commands:
```
cd "C:\Users\Vikash Mishra\eclipse-workspace\JenkinsDeclarativePipelineWithTestNG"
git add .
git commit -m "Fix Jenkinsfile - parallel syntax and triggers"
git push origin main