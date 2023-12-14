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
    >&2 echo "Usage: $0 <website-version-to-set-as-default> <eclipse-account-full-name> <eclipse-account-email>"
    exit 1
fi

# Get arguments. For the website version to make default, remove any trailing '/' if present.
VERSION_TO_MAKE_DEFAULT=${1%/}
ECLIPSE_ACCOUNT_FULLNAME=$2
ECLIPSE_ACCOUNT_EMAIL=$3

# Make sure 'nightly' is not made the standard visible website.
if [ "${VERSION_TO_MAKE_DEFAULT}" == "nightly" ]; then
    >&2 echo "Making 'nightly' the standard visible website is not supported."
    exit 1
fi

# Make sure the website version to make default exists.
if [ ! -d ${VERSION_TO_MAKE_DEFAULT} ]; then
    >&2 echo "Website directory \"${VERSION_TO_MAKE_DEFAULT}\" does not exist. Failed to make it the default website."
    exit 1
fi

# Configure filenames.
FILENAME_CONTENTS=.standard_visible_website_contents
FILENAME_VERSION=.standard_visible_website_version

# Remove files for current standard visible website.
echo "Removing files of current standard visible website..."
TO_REMOVE=$(cat $FILENAME_CONTENTS)
rm -rf $TO_REMOVE

# Copy files for new standard visible website.
echo "Copying files of new standard visible website..."
cp -r ${VERSION_TO_MAKE_DEFAULT}/* .

# Update metadata files.
echo "Updating metadata files..."
ls -1 ${VERSION_TO_MAKE_DEFAULT} | sort > $FILENAME_CONTENTS
echo ${VERSION_TO_MAKE_DEFAULT} > $FILENAME_VERSION

# Replace version-specific URLs.
echo "Replacing version-specific URLs in HTML files..."
WEBSITES_FILES_AND_DIRS=$(cat $FILENAME_CONTENTS)
find $WEBSITES_FILES_AND_DIRS -type f -iname "*.html" -print0 | \
    xargs -0 sed -i "s@https://eclipse.dev/escet/${VERSION_TO_MAKE_DEFAULT}/@https://eclipse.dev/escet/@g"
find $WEBSITES_FILES_AND_DIRS -type f -iname "*.html" -print0 | \
    xargs -0 sed -i "s@https://www.eclipse.dev/escet/${VERSION_TO_MAKE_DEFAULT}/@https://eclipse.dev/escet/@g"

# Remove robots meta tag.
echo "Removing robots meta tag in HTML files..."
find $WEBSITES_FILES_AND_DIRS -type f -iname "*.html" -print0 | \
    xargs -0 sed -i "s/<meta name=\"robots\" content=\"noindex\">//"

# Stage all changes.
git add -A

# Check for changes.
echo "Checking for changes..."
set +e
git diff-index --quiet --exit-code HEAD
CHANGES=$?
set -e

# Commit changes, if any.
if [ $CHANGES -eq 1 ]; then
    echo "Committing changes..."
    COMMIT_MSG="Set standard visible website to release ${VERSION_TO_MAKE_DEFAULT}."
    GIT_COMMITTER_NAME="$ECLIPSE_ACCOUNT_FULLNAME" GIT_COMMITTER_EMAIL="$ECLIPSE_ACCOUNT_EMAIL" \
            git commit --author="$ECLIPSE_ACCOUNT_FULLNAME <$ECLIPSE_ACCOUNT_EMAIL>" -m "${COMMIT_MSG}"
    echo
    echo ${COMMIT_MSG}
    echo "Review the last commit before pushing it."
else
    echo
    echo "The website for release ${VERSION_TO_MAKE_DEFAULT} is already the standard visible website."
fi
