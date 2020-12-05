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
        jdk 'adoptopenjdk-hotspot-jdk8-latest'
        maven 'apache-maven-latest'
    }
    options {
        timeout(time: 2, unit: 'HOURS')
        timestamps()
    }
    stages {
        stage('Info') {
            steps {
                sh '''
                    java -version
                    mvn -version
                '''
            }
        }
        stage('Build') {
            steps {
                sh '''
                    mvn -Dtycho.pomless.aggregator.names=releng,chi,cif,common,setext,tooldef,products -pl !cif/org.eclipse.escet.cif.datasynth,!cif/org.eclipse.escet.cif.datasynth.tests clean package
                '''
            }
        }
        stage('Test') {
            steps {
                wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                    sh '''
                        mvn -Dtycho.pomless.aggregator.names=releng,chi,cif,common,setext,tooldef,products -pl !cif/org.eclipse.escet.cif.datasynth,!cif/org.eclipse.escet.cif.datasynth.tests verify
                    '''
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
