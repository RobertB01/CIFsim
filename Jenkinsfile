//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

pipeline {
    agent {
        // The 'centos-7' pod template allows UI tests.
        kubernetes {
            label 'centos-7'
        }
    }

    tools {
        jdk 'oracle-jdk8-latest'
        maven 'apache-maven-3.6.3'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout(time: 1, unit: 'HOURS')
        timestamps()
    }

    stages {
        stage('Build & Test') {
            steps {
                sh 'printenv'
                wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                    sh '''
                        java -version
                        mvn -version

                        BUILD_ARGS=
                        if [ "$GIT_BRANCH" == "5-add-jar-signing-to-build" ]; then
                            BUILD_ARGS = -Psign
                        fi

                        ./build.sh $BUILD_ARGS
                    '''
                }
            }

            post {
                success {
                    // Documentation.
                    archiveArtifacts '*/org.eclipse.escet.*documentation/target/*.zip'

                    // Update site.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/*.zip'

                    // Product.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*.tar.gz'
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*.zip'
                }
            }
        }
    }

    post {
        // Send an e-mail on unsuccessful builds (unstable, failure, aborted).
        unsuccessful {
            emailext subject: 'Build $BUILD_STATUS $PROJECT_NAME #$BUILD_NUMBER!',
            body: '''Check console output at $BUILD_URL to view the results.''',
            recipientProviders: [culprits(), requestor()]
        }

        // Send an e-mail on fixed builds (back to normal).
        fixed {
            emailext subject: 'Build $BUILD_STATUS $PROJECT_NAME #$BUILD_NUMBER!',
            body: '''Check console output at $BUILD_URL to view the results.''',
            recipientProviders: [culprits(), requestor()]
        }
    }
}
