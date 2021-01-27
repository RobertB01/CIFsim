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
                        printenv

                        BUILD_ARGS=
                        if [ "$GIT_BRANCH" == "master" ]; then
                            # Sign only for releases, on 'master' branch.
                            BUILD_ARGS="-Psign"
                        fi

                        ./build.sh "$BUILD_ARGS"
                    '''
                }
            }

            post {
                success {
                    // Documentation/websites.
                    archiveArtifacts '*/org.eclipse.escet.*documentation/target/*-website.zip'

                    // Update site.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/*.zip'

                    // Product.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*.tar.gz'
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*.zip'
                }
            }
        }

        stage('Deploy') {
            when {
                branch '7-configure-deployment-to-eclipse-foundation-infrastructure' //XXX change to master
            }
            environment {
                DOWNLOADS_PATH = "/home/data/httpd/download.eclipse.org/escet"
                DOWNLOADS_URL = "genie.escet@projects-storage.eclipse.org:${DOWNLOADS_PATH}"
                RELEASE_VERSION = "test" //XXX determine actual release version
            }
            steps {
                // Deploy downloads.
                sh '''
                    mkdir -p deploy/update-site/
                    unzip -q products/org.eclipse.escet.product/target/*.zip -d deploy/update-site/
                '''
                sshagent (['projects-storage.eclipse.org-bot-ssh']) {
                    // Remove any existing directory for this release.
                    sh 'ssh genie.escet@projects-storage.eclipse.org rm -rf ${DOWNLOADS_PATH}/${RELEASE_VERSION}/'

                    // Create directory for this release.
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/'

                    // Documentation/websites.
                    //XXX artifacts don't have a qualifier, just SNAPSHOT
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/websites/'
                    sh 'scp -r */org.eclipse.escet.*documentation/target/*-website.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/websites/'

                    // Update site (archive).
                    sh 'scp -r products/org.eclipse.escet.product/target/*.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/'

                    // Update site (extracted).
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/update-site/'
                    sh 'scp -r deploy/update-site/* ${DOWNLOADS_URL}/${RELEASE_VERSION}/update-site/'

                    // Product.
                    sh 'scp -r products/org.eclipse.escet.product/target/products/*.tar.gz ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                    sh 'scp -r products/org.eclipse.escet.product/target/products/*.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                }

                // Deploy websites.
                sshagent(['git.eclipse.org-bot-ssh']) {
                    sh '''
                        mkdir -p deploy/www
                        git clone ssh://genie.escet@git.eclipse.org:29418/www.eclipse.org/escet.git deploy/www

                        rm -rf deploy/www/${RELEASE_VERSION}
                        mkdir -p deploy/www/${RELEASE_VERSION}
                        unzip -q products/org.eclipse.escet.documentation/target/*-website.zip -d deploy/www/${RELEASE_VERSION}/escet/
                        unzip -q chi/org.eclipse.escet.chi.documentation/target/*-website.zip -d deploy/www/${RELEASE_VERSION}/chi/
                        unzip -q cif/org.eclipse.escet.cif.documentation/target/*-website.zip -d deploy/www/${RELEASE_VERSION}/cif/
                        unzip -q setext/org.eclipse.escet.setext.documentation/target/*-website.zip -d deploy/www/${RELEASE_VERSION}/setext/
                        unzip -q tooldef/org.eclipse.escet.tooldef.documentation/target/*-website.zip -d deploy/www/${RELEASE_VERSION}/tooldef/
                    '''
                    //  XXX remove all 'test' words.
                    dir('deploy/www') {
                        sh '''
                            git config user.email "escet-bot@eclipse.org"
                            git config user.name "genie.escet"
                            git config push.default simple # Required to silence Git push warning.
                            git add -A
                            git commit -q -m "Website release test ${RELEASE_VERSION}."
                            git push
                        '''
                    }
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
