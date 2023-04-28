//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
            inheritFrom 'centos-7'
        }
    }

    tools {
        jdk 'openjdk-jdk17-latest'
        maven 'apache-maven-3.8.6'
    }

    options {
        buildDiscarder(logRotator(
            // Number of builds to keep.
            numToKeepStr: '5',

            // Number of builds for which to keep the artifacts.
            artifactNumToKeepStr: '5',

            // Number of days to keep builds.
            daysToKeepStr: env.BRANCH_NAME ==~ /master/ ? '7' :             // master
                           env.BRANCH_NAME ==~ /develop/ ? '1000' :         // develop
                           env.TAG_NAME ==~ /v[0-9]+\\.[0-9]+.*/ ?  '120' : // release tags
                           '30',                                            // other branches and merge requests

            // Number of days to keep artifacts of builds.
            artifactDaysToKeepStr: env.BRANCH_NAME ==~ /master/ ? '7' :            // master
                                   env.BRANCH_NAME ==~ /develop/ ? '1000' :        // develop
                                   env.TAG_NAME ==~ /v[0-9]+\\.[0-9]+.*/ ?  '30' : // release tags
                                   '30',                                           // other branches and merge requests
        ))
        timeout(time: 1, unit: 'HOURS')
        timestamps()
    }

    stages {
        stage('Build & Test') {
            steps {
                wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                    sh '''
                        # Print versions.
                        java -version
                        mvn -version
                        git --version

                        # Print environment.
                        printenv

                        # Check license headers are present for all files, where relevant.
                        ./misc/license-header/license-header-check.bash

                        # Get Git last commit date.
                        GIT_DATE_EPOCH=$(git log -1 --format=%cd --date=raw | cut -d ' ' -f 1)
                        GIT_DATE=$(date -d @$GIT_DATE_EPOCH -u +%Y%m%d-%H%M%S)

                        # Configure 'jenkins' profile for build.
                        BUILD_ARGS="-Pjenkins"

                        # Configure 'sign' profile for build.
                        # Sign 'master' branch, to allow checking release signing before deployment.
                        # Sign releases. Determined based on release version tag name.
                        if [[ "$GIT_BRANCH" == "master" || "$TAG_NAME" =~ ^v[0-9]+\\.[0-9]+.*$ ]]; then
                            BUILD_ARGS="$BUILD_ARGS -Psign"
                        fi

                        # Override the 'escet.version.enduser' property for releases. Remains 'dev' otherwise.
                        if [[ "$TAG_NAME" =~ ^v[0-9]+\\.[0-9]+.*$ ]]; then
                            BUILD_ARGS="$BUILD_ARGS -Descet.version.enduser=$TAG_NAME"
                        fi

                        # Override the 'escet.website.version' property for releases. Remains '' otherwise.
                        if [[ "$TAG_NAME" =~ ^v[0-9]+\\.[0-9]+.*$ ]]; then
                            BUILD_ARGS="$BUILD_ARGS -Descet.website.version=$TAG_NAME"
                        fi

                        # Override the 'escet.version.qualifier' property for Jenkins builds.
                        # It starts with 'v' and the Git date, followed by a qualifier postfix.
                        # For releases, the qualifier postfix is the postfix of the version tag (if any).
                        # For non-releases, the qualifier postfix is 'dev'.
                        if [[ "$TAG_NAME" =~ ^v[0-9]+\\.[0-9]+.*$ ]]; then
                            QUALIFIER_POSTFIX=$(echo "$TAG_NAME" | sed -e 's/^[^-]*//g')
                        else
                            QUALIFIER_POSTFIX=-dev
                        fi
                        BUILD_ARGS="$BUILD_ARGS -Descet.version.qualifier=v$GIT_DATE$QUALIFIER_POSTFIX"

                        # Perform build.
                        ./build.sh $BUILD_ARGS
                    '''
                }
            }

            post {
                success {
                    // Website.
                    archiveArtifacts 'releng/org.eclipse.escet.releng.website/target/eclipse-escet-*-website.zip'

                    // Update site.
                    archiveArtifacts 'product/org.eclipse.escet.product/target/*-updatesite.zip'

                    // Product.
                    archiveArtifacts 'product/org.eclipse.escet.product/target/products/*-linux*.tar.gz'
                    archiveArtifacts 'product/org.eclipse.escet.product/target/products/*-mac*.dmg'
                    archiveArtifacts 'product/org.eclipse.escet.product/target/products/*-win*.zip'

                    // Code coverage.
                    archiveArtifacts 'releng/org.eclipse.escet.releng.tests/target/eclipse-escet-jacoco-aggregate.zip'
                }
            }
        }

        stage('Deploy') {
            when {
                tag pattern: "v\\d+\\.\\d+.*", comparator: "REGEXP"
            }
            environment {
                DOWNLOADS_PATH = "/home/data/httpd/download.eclipse.org/escet"
                DOWNLOADS_URL = "genie.escet@projects-storage.eclipse.org:${DOWNLOADS_PATH}"
                WEBSITE_GIT_URL = "git@gitlab.eclipse.org:eclipse/escet/escet-website.git"
                RELEASE_VERSION = "${TAG_NAME}"
            }
            steps {
                // Deploy downloads.
                sh '''
                    mkdir -p deploy/update-site/
                    unzip -q product/org.eclipse.escet.product/target/*-updatesite.zip -d deploy/update-site/
                '''
                sshagent (['projects-storage.eclipse.org-bot-ssh']) {
                    // Remove any existing directory for this release.
                    sh 'ssh genie.escet@projects-storage.eclipse.org rm -rf ${DOWNLOADS_PATH}/${RELEASE_VERSION}/'

                    // Create directory for this release.
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/'

                    // Website.
                    sh 'scp -r releng/org.eclipse.escet.releng.website/target/eclipse-escet-*-website.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/'

                    // Update site (archive).
                    sh 'scp -r product/org.eclipse.escet.product/target/*-updatesite.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/'

                    // Update site (extracted).
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/update-site/'
                    sh 'scp -r deploy/update-site/* ${DOWNLOADS_URL}/${RELEASE_VERSION}/update-site/'

                    // Product.
                    sh 'scp -r product/org.eclipse.escet.product/target/products/*-linux*.tar.gz ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                    sh 'scp -r product/org.eclipse.escet.product/target/products/*-mac*.dmg ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                    sh 'scp -r product/org.eclipse.escet.product/target/products/*-win*.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                }

                // Deploy website.
                sshagent(['gitlab-bot-ssh']) {
                    sh '''
                        mkdir -p deploy/www
                        git clone ${WEBSITE_GIT_URL} deploy/www

                        rm -rf deploy/www/${RELEASE_VERSION}
                        mkdir -p deploy/www/${RELEASE_VERSION}
                        unzip -q releng/org.eclipse.escet.releng.website/target/eclipse-escet-*-website.zip -d deploy/www/${RELEASE_VERSION}/
                    '''
                    dir('deploy/www') {
                        sh '''
                            git config user.email "escet-bot@eclipse.org"
                            git config user.name "genie.escet"
                            git config push.default simple # Required to silence Git push warning.
                            git add -A
                            git commit -q -m "Website release ${RELEASE_VERSION}." -m "Generated from commit ${GIT_COMMIT}"
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
