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
                wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                    sh '''
                        java -version
                        mvn -version
                        ./build.sh
                    '''
                }
            }

            post {
                success {
                    // Chi documentation.
                    archiveArtifacts 'chi/org.eclipse.escet.chi.documentation/target/*.jar'
                    archiveArtifacts 'chi/org.eclipse.escet.chi.documentation/target/*.zip'

                    // CIF documentation.
                    archiveArtifacts 'cif/org.eclipse.escet.cif.documentation/target/*.jar'
                    archiveArtifacts 'cif/org.eclipse.escet.cif.documentation/target/*.zip'

                    // Project/product documentation.
                    archiveArtifacts 'products/org.eclipse.escet.documentation/target/*.jar'
                    archiveArtifacts 'products/org.eclipse.escet.documentation/target/*.zip'

                    // Update site.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/*.jar'
                    archiveArtifacts 'products/org.eclipse.escet.product/target/*.zip'

                    // Product.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*.tar.gz'
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*.zip'

                    // SeText documentation.
                    archiveArtifacts 'setext/org.eclipse.escet.setext.documentation/target/*.jar'
                    archiveArtifacts 'setext/org.eclipse.escet.setext.documentation/target/*.zip'

                    // ToolDef documentation.
                    archiveArtifacts 'tooldef/org.eclipse.escet.setext.documentation/target/*.jar'
                    archiveArtifacts 'tooldef/org.eclipse.escet.setext.documentation/target/*.zip'
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
