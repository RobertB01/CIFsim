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
        timeout(time: 1, unit: 'HOURS')
        timestamps()
    }
    stages {
        stage('Build') {
            steps {
                sh '''
                    java -version
                    mvn -version
                '''
            }
            steps {
                wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                    sh '''
                        ./build.sh
                    '''
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
