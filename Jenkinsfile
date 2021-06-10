//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
        jdk 'openjdk-jdk11-latest'
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

                        #XXX TMP test
                        TAG_NAME=v0.2-RC1

                        # Override the 'escet.version.enduser' property for releases. Remains 'dev' otherwise.
                        if [[ "$TAG_NAME" =~ ^v[0-9]+\\.[0-9]+.*$ ]]; then
                            BUILD_ARGS="$BUILD_ARGS -Descet.version.enduser=$TAG_NAME"
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
                    // Documentation/websites.
                    archiveArtifacts '*/org.eclipse.escet.*documentation/target/*-website-*.zip'

                    // Update site.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/*-updatesite-*.zip'

                    // Product.
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*-linux*.tar.gz'
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*-mac*.dmg'
                    archiveArtifacts 'products/org.eclipse.escet.product/target/products/*-win*.zip'
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
                WEBSITE_GIT_URL = "ssh://genie.escet@git.eclipse.org:29418/www.eclipse.org/escet.git"
                RELEASE_VERSION = "${TAG_NAME}"
            }
            steps {
                // Deploy downloads.
                sh '''
                    mkdir -p deploy/update-site/
                    unzip -q products/org.eclipse.escet.product/target/*-updatesite-*.zip -d deploy/update-site/
                '''
                sshagent (['projects-storage.eclipse.org-bot-ssh']) {
                    // Remove any existing directory for this release.
                    sh 'ssh genie.escet@projects-storage.eclipse.org rm -rf ${DOWNLOADS_PATH}/${RELEASE_VERSION}/'

                    // Create directory for this release.
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/'

                    // Documentation/websites.
                    // NOTE: for these artifacts the qualifier is 'SNAPSHOT' rather than the actual version qualifier.
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/websites/'
                    sh 'scp -r */org.eclipse.escet.*documentation/target/*-website-*.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/websites/'

                    // Update site (archive).
                    sh 'scp -r products/org.eclipse.escet.product/target/*-updatesite-*.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/'

                    // Update site (extracted).
                    sh 'ssh genie.escet@projects-storage.eclipse.org mkdir -p ${DOWNLOADS_PATH}/${RELEASE_VERSION}/update-site/'
                    sh 'scp -r deploy/update-site/* ${DOWNLOADS_URL}/${RELEASE_VERSION}/update-site/'

                    // Product.
                    sh 'scp -r products/org.eclipse.escet.product/target/products/*-linux*.tar.gz ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                    sh 'scp -r products/org.eclipse.escet.product/target/products/*-mac*.dmg ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                    sh 'scp -r products/org.eclipse.escet.product/target/products/*-win*.zip ${DOWNLOADS_URL}/${RELEASE_VERSION}/'
                }

                // Deploy websites.
                sshagent(['git.eclipse.org-bot-ssh']) {
                    sh '''
                        mkdir -p deploy/www
                        git clone ${WEBSITE_GIT_URL} deploy/www

                        rm -rf deploy/www/${RELEASE_VERSION}
                        mkdir -p deploy/www/${RELEASE_VERSION}
                        cp releng/website/*.html deploy/www/${RELEASE_VERSION}
                        cp releng/website/*.png deploy/www/${RELEASE_VERSION}
                        sed -i -e "s/@VERSION@/${RELEASE_VERSION}/g" deploy/www/${RELEASE_VERSION}/index.html
                        unzip -q products/org.eclipse.escet.documentation/target/*-website-*.zip -d deploy/www/${RELEASE_VERSION}/escet/
                        unzip -q chi/org.eclipse.escet.chi.documentation/target/*-website-*.zip -d deploy/www/${RELEASE_VERSION}/chi/
                        unzip -q cif/org.eclipse.escet.cif.documentation/target/*-website-*.zip -d deploy/www/${RELEASE_VERSION}/cif/
                        unzip -q setext/org.eclipse.escet.setext.documentation/target/*-website-*.zip -d deploy/www/${RELEASE_VERSION}/setext/
                        unzip -q tooldef/org.eclipse.escet.tooldef.documentation/target/*-website-*.zip -d deploy/www/${RELEASE_VERSION}/tooldef/
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
