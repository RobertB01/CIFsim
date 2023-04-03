#!/usr/bin/env bash

################################################################################
# Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################

# For usage information, see the Eclipse ESCET developer documentation 'Release process' page.

set -e -u
#set -x

# Check command line arguments.
if [ $# -ne 3 ]; then
    >&2 echo "Usage: $0 <website-version-to-remove> <eclipse-account-full-name> <eclipse-account-email>"
    exit 1
fi

# Get arguments. For the website version to remove, remove any trailing '/' if present.
VERSION_TO_REMOVE=${1%/}
ECLIPSE_ACCOUNT_FULLNAME=$2
ECLIPSE_ACCOUNT_EMAIL=$3

# Make sure the website version to remove exists.
if [ ! -d ${VERSION_TO_REMOVE} ]; then
    >&2 echo "Website directory \"${VERSION_TO_REMOVE}\" does not exist. Failed to remove it."
    exit 1
fi

# Ensure version being removed is not the current standard visible website.
CUR_VISIBLE_VERSION=$(<.standard_visible_website_version)
if [ "${VERSION_TO_REMOVE}" == "${CUR_VISIBLE_VERSION}" ]; then
    >&2 echo "Website directory \"${VERSION_TO_REMOVE}\" is the current standard visible website. It can not be removed."
    exit 1
fi

# Ensure version being removed is a milestone or release candidate website.
if ! [[ "${VERSION_TO_REMOVE}" =~ ^v[0-9]+(\.[0-9]+){1,2}-(M|RC)[0-9]+$ ]]; then
    >&2 echo "Website directory \"${VERSION_TO_REMOVE}\" can not be removed, as it is not a milestone or release candidate website."
    exit 1
fi

# Remove website directory.
echo "Removing website directory..."
rm -rf ${VERSION_TO_REMOVE}/

# Stage all changes.
git add -A

# Commit changes.
echo "Committing changes..."
COMMIT_MSG="Removed website version ${VERSION_TO_REMOVE}."
GIT_COMMITTER_NAME="$ECLIPSE_ACCOUNT_FULLNAME" GIT_COMMITTER_EMAIL="$ECLIPSE_ACCOUNT_EMAIL" \
        git commit --author="$ECLIPSE_ACCOUNT_FULLNAME <$ECLIPSE_ACCOUNT_EMAIL>" -m "${COMMIT_MSG}"
echo
echo ${COMMIT_MSG}
echo "Review the last commit before pushing it."
