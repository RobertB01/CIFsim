#!/usr/bin/env bash

################################################################################
# Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################# 

# This script is only meant to push to branches on the Eclipse Foundation
# GitLab server (gitlab.eclipse.org) that are forks of the official Eclipse
# ESCET Git repository.

# Usage: misc/fork-collab/fork-push-branch.bash <username> <branch_name>

set -e -u

SCRIPT=`readlink -f $0`
SCRIPTPATH=`dirname $SCRIPT`
cd $SCRIPTPATH

if [ $# -ne 2 ]
then
    echo "Usage: $0 <username> <branch_name>"
    exit 1
fi

GITLAB_USERNAME=$1
GITLAB_BRANCH_NAME=$2
GITLAB_URL=https://gitlab.eclipse.org/$GITLAB_USERNAME/escet.git
LOCAL_BRANCH_NAME=$GITLAB_USERNAME/escet/$GITLAB_BRANCH_NAME

echo "Username:                  $GITLAB_USERNAME"
echo "GitLab remote branch name: $GITLAB_BRANCH_NAME"
echo "GitLab URL:                $GITLAB_URL"
echo "Local branch name:         $LOCAL_BRANCH_NAME"

git push "$GITLAB_URL" "$LOCAL_BRANCH_NAME":"$GITLAB_BRANCH_NAME"
