// ============================================================
//  DECLARATIVE PIPELINE — Jenkins QA Interview Practice
//  Covers: Parameters, Environment Variables, Credentials,
//          Stages, Parallel Execution, Post Actions,
//          Triggers, TestNG Report Publishing
// ============================================================

pipeline {

    // ---------------------------------------------------------
    // AGENT
    // Interview Q: What does agent any mean?
    // A: Run this pipeline on ANY available Jenkins agent/node.
    //    Use agent { label 'linux' } to target a specific node.
    // ---------------------------------------------------------
    agent any

    // ---------------------------------------------------------
    // TRIGGERS
    // Interview Q: How do you schedule a Jenkins job?
    // A: Using cron inside triggers{}
    //
    // Interview Q: What is pollSCM?
    // A: Jenkins checks your Git repo at intervals. If new commit
    //    is found, it triggers the build automatically.
    //
    // Uncomment the one you want to test:
    // ---------------------------------------------------------
    triggers {
        // Runs every day at midnight
        // cron('H 0 * * *')

        // Polls Git repo every 5 minutes for new commits
        // pollSCM('H/5 * * * *')

        // Both together (common real-world setup):
        // cron('H 0 * * *')
        // pollSCM('H/5 * * * *')
    }

    // ---------------------------------------------------------
    // PARAMETERS
    // Interview Q: What are parameterized builds in Jenkins?
    // A: They let you pass dynamic values at runtime so the same
    //    pipeline can run for different envs, browsers, suites.
    //
    // Interview Q: How do you access a parameter in pipeline?
    // A: Use params.PARAM_NAME  e.g. params.ENV
    // ---------------------------------------------------------
    parameters {
        choice(
            name: 'ENV',
            choices: ['dev', 'staging', 'prod'],
            description: 'Target environment for the test run'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge'],
            description: 'Browser to run tests on'
        )
        choice(
            name: 'SUITE',
            choices: ['regression', 'smoke'],
            description: 'TestNG suite to execute'
        )
        booleanParam(
            name: 'FORCE_FAILURE',
            defaultValue: false,
            description: 'Set true to intentionally fail CheckoutTests.testPaymentProcessing (demo)'
        )
        string(
            name: 'THREAD_COUNT',
            defaultValue: '3',
            description: 'Number of parallel threads for TestNG suite'
        )
    }

    // ---------------------------------------------------------
    // ENVIRONMENT VARIABLES
    // Interview Q: Difference between params and environment vars?
    // A: params = user input at build time (parameters block)
    //    env    = variables available throughout pipeline
    //    You can COMBINE both (see below)
    //
    // Interview Q: How do you use Jenkins credentials securely?
    // A: credentials() binding — value is masked in console logs
    // ---------------------------------------------------------
    environment {
        // Static env variable
        PROJECT_NAME = 'jenkins-testng-practice'
        MAVEN_OPTS   = '-Xmx512m'

        // Combining parameter value into env variable
        TEST_ENV     = "${params.ENV}"
        REPORT_DIR   = "target/surefire-reports"

        // Credentials — stored in Jenkins > Manage Credentials
        // Interview Q: Why use credentials() instead of hardcoding?
        // A: Credentials are masked in logs, stored encrypted.
        //    Never hardcode passwords in Jenkinsfile!
        //
        // To practice: Go to Jenkins > Manage Jenkins > Credentials
        // Add a "Secret text" with ID = dummy-api-key, value = ABC123
        // Then uncomment the line below:
        // API_KEY = credentials('dummy-api-key')
    }

    // ---------------------------------------------------------
    // OPTIONS
    // Interview Q: What is buildDiscarder? What is timeout?
    // ---------------------------------------------------------
    options {
        // Keep only last 10 builds (saves disk space)
        buildDiscarder(logRotator(numToKeepStr: '10'))

        // Fail the build if it takes more than 30 minutes
        timeout(time: 30, unit: 'MINUTES')

        // Adds timestamps to every console log line
        timestamps()

        // Don't allow concurrent builds of same job
        disableConcurrentBuilds()
    }

    // =========================================================
    // STAGES — The heart of the pipeline
    // Interview Q: What is a stage in Jenkins pipeline?
    // A: A logical step/phase shown in the Jenkins Blue Ocean UI.
    // =========================================================
    stages {

        // -------------------------------------------------
        // STAGE 1: PRINT BUILD INFO
        // Interview Q: How do you print environment/parameter
        //              values in a Jenkins pipeline?
        // -------------------------------------------------
        stage('Print Build Info') {
            steps {
                echo '============================================'
                echo "Job Name    : ${env.JOB_NAME}"
                echo "Build Number: ${env.BUILD_NUMBER}"
                echo "Build URL   : ${env.BUILD_URL}"
                echo "Workspace   : ${env.WORKSPACE}"
                echo "Node Name   : ${env.NODE_NAME}"
                echo "--------------------------------------------"
                echo "ENV         : ${params.ENV}"
                echo "BROWSER     : ${params.BROWSER}"
                echo "SUITE       : ${params.SUITE}"
                echo "FORCE FAIL  : ${params.FORCE_FAILURE}"
                echo "THREADS     : ${params.THREAD_COUNT}"
                echo '============================================'
            }
        }

        // -------------------------------------------------
        // STAGE 2: CHECKOUT
        // Interview Q: What does checkout scm do?
        // A: Checks out the source code from the SCM (Git) repo
        //    configured in the Jenkins job. scm = auto-detect.
        // -------------------------------------------------
        stage('Checkout') {
            steps {
                echo '[Checkout] Checking out source code...'
                // In a real project with Git repo, use:
                // checkout scm
                // OR explicitly:
                // git url: 'https://github.com/yourrepo/project.git', branch: 'main'

                // For local practice (no Git), just print:
                echo '[Checkout] Using local workspace — no Git configured'
                echo "[Checkout] Workspace: ${env.WORKSPACE}"
            }
        }

        // -------------------------------------------------
        // STAGE 3: BUILD / COMPILE
        // Interview Q: How do you run Maven in Jenkins?
        // A: Use sh 'mvn ...' on Linux or bat 'mvn ...' on Windows
        // -------------------------------------------------
        stage('Build') {
            steps {
                echo '[Build] Compiling the project...'
                // Linux:
                sh 'mvn clean compile -q'
                // Windows (uncomment if running on Windows agent):
                // bat 'mvn clean compile -q'
                echo '[Build] Compilation successful'
            }
        }

        // -------------------------------------------------
        // STAGE 4: PARALLEL TEST EXECUTION
        // Interview Q: How do you run tests in parallel in Jenkins?
        // A: Use parallel{} block inside a stage. Each branch runs
        //    simultaneously on the same or different agents.
        //
        // Interview Q: What is the difference between TestNG parallel
        //              and Jenkins parallel?
        // A: TestNG parallel = threads within one Maven run (in suite XML)
        //    Jenkins parallel = separate pipeline branches (can use diff nodes)
        // -------------------------------------------------
        stage('Run Tests - Parallel') {
            parallel {

                // Branch 1: Smoke Tests
                branch('Smoke Tests') {
                    steps {
                        echo '[Smoke] Starting smoke test run...'
                        sh """
                            mvn test \
                                -Dsuite=smoke \
                                -Denv=${params.ENV} \
                                -Dbrowser=${params.BROWSER} \
                                -DforceFailure=false \
                                -q
                        """
                        echo '[Smoke] Smoke tests completed'
                    }
                }

                // Branch 2: Regression Tests
                branch('Regression Tests') {
                    steps {
                        echo '[Regression] Starting regression test run...'
                        sh """
                            mvn test \
                                -Dsuite=regression \
                                -Denv=${params.ENV} \
                                -Dbrowser=${params.BROWSER} \
                                -DforceFailure=${params.FORCE_FAILURE} \
                                -q
                        """
                        echo '[Regression] Regression tests completed'
                    }
                }
            }
        }

        // -------------------------------------------------
        // STAGE 5: GENERATE REPORT SUMMARY
        // Interview Q: How do you publish TestNG/HTML reports?
        // A: Use the TestNG Results plugin or HTML Publisher plugin.
        //    publishHTML([]) or step([$class: 'Publisher'...])
        //
        // Requires: "TestNG Results Plugin" installed in Jenkins
        // -------------------------------------------------
        stage('Publish Reports') {
            steps {
                echo '[Reports] Publishing TestNG test results...'

                // Publish TestNG XML results (requires TestNG Results Plugin)
                // This shows pass/fail/skip counts in Jenkins UI
                step([$class: 'Publisher',
                      reportFilenamePattern: '**/target/surefire-reports/testng-results.xml'
                ])

                // Publish HTML report (requires HTML Publisher Plugin)
                publishHTML([
                    allowMissing         : false,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'target/surefire-reports',
                    reportFiles          : 'index.html',
                    reportName           : 'TestNG HTML Report',
                    reportTitles         : 'Test Results'
                ])

                echo '[Reports] Reports published successfully'
            }
        }

    } // end stages


    // =========================================================
    // POST ACTIONS
    // Interview Q: What is the post{} block in Jenkins?
    // A: Steps that run AFTER all stages, based on build result.
    //    always   = runs no matter what
    //    success  = runs only if build passed
    //    failure  = runs only if build failed
    //    unstable = runs if build is unstable (e.g. test failures)
    //    changed  = runs if result changed from last build
    //
    // Interview Q: Difference between failure and unstable?
    // A: failure  = pipeline/stage itself crashed (compile error etc)
    //    unstable = pipeline ran but test results had failures
    // =========================================================
    post {

        always {
            echo '--------------------------------------------'
            echo "[POST-ALWAYS] Build #${env.BUILD_NUMBER} completed"
            echo "[POST-ALWAYS] Result: ${currentBuild.result ?: 'SUCCESS'}"
            echo "[POST-ALWAYS] Duration: ${currentBuild.durationString}"
            echo '--------------------------------------------'

            // Archive test result XMLs for later inspection
            archiveArtifacts artifacts: '**/target/surefire-reports/**/*.xml',
                             allowEmptyArchive: true

            // JUnit-compatible results (Jenkins reads TestNG XML as JUnit)
            // Interview Q: Why do we use junit step for TestNG results?
            // A: Jenkins natively understands JUnit XML format.
            //    TestNG also produces compatible XML. This populates
            //    the "Test Result" trend chart on the job page.
            junit testResults: '**/target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }

        success {
            echo '[POST-SUCCESS] ✅ All stages passed! Great run.'
            // In real projects: send Slack/email notification here
            // slackSend color: 'good', message: "Build ${env.BUILD_NUMBER} passed"
        }

        unstable {
            echo '[POST-UNSTABLE] ⚠️  Build is UNSTABLE — some tests failed.'
            echo '[POST-UNSTABLE] Check the TestNG report for failed test details.'
            // slackSend color: 'warning', message: "Build ${env.BUILD_NUMBER} unstable"
        }

        failure {
            echo '[POST-FAILURE] ❌ Build FAILED — a stage crashed (not just tests).'
            echo '[POST-FAILURE] Check console output for errors.'
            // slackSend color: 'danger', message: "Build ${env.BUILD_NUMBER} failed"
        }

        changed {
            echo "[POST-CHANGED] Build result changed from last run."
            echo "[POST-CHANGED] Previous: ${currentBuild.previousBuild?.result}"
            echo "[POST-CHANGED] Current : ${currentBuild.result ?: 'SUCCESS'}"
        }

    } // end post

} // end pipeline
