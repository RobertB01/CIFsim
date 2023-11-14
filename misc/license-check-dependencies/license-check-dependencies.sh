#!/usr/bin/env sh

################################################################################
# Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################

# Go to Git repository root.
SCRIPT=`readlink -f $0`
SCRIPTPATH=`dirname $SCRIPT`
cd $SCRIPTPATH/../..

# Clean up from last time.
rm -rf DEPENDENCIES.generated.txt
rm -rf DEPENDENCIES.generated.processed.txt

# Perform license check.
set -e
mvn org.eclipse.dash:license-tool-plugin:license-check -Ddash.projectId=technology.escet -Ddash.summary=DEPENDENCIES.generated.txt -Dtycho.target.eager=true $*
set +e
cat DEPENDENCIES.generated.txt | grep -v "^maven/mavencentral/org.eclipse.escet/" > DEPENDENCIES.generated.processed.txt

# Check for differences with stored license check result.
# Exit code is zero in case of no differences.
echo
echo "Checking for differences between generated and stored dependency lists..."
diff --strip-trailing-cr -u DEPENDENCIES.txt DEPENDENCIES.generated.processed.txt
ANY_DIFFS=$?

# Check for restricted dependencies.
# Exit code is 0 in case of some restricted entries, 1 in case of no restricted entries, and some other value in case of error.
echo
echo "Checking for restricted dependencies..."
grep restricted DEPENDENCIES.generated.processed.txt
RESTRICTED=$?

# Fail on differences and/or restricted dependencies.
SCRIPT_EXIT_CODE=0
if [ $ANY_DIFFS -ne 0 ]; then
    >&2 echo
    >&2 echo "FAILED: Differences found:"
    >&2 echo " - Please replace DEPENDENCIES.txt by DEPENDENCIES.generated.processed.txt."
    SCRIPT_EXIT_CODE=1
fi
if [ $RESTRICTED -ne 1 ]; then
    >&2 echo
    >&2 echo "FAILED: Restricted dependencies found. Please:"
    >&2 echo " - If applicable, automatically create review requests on the IP Team GitLab."
    >&2 echo "   To do so, run this script again, appending '-Ddash.iplab.token=<your_personal_token>'"
    >&2 echo "   (without the quotes) as an extra argument. For more information on this feature,"
    >&2 echo "   as well as how to create your personal token that is to be filled in, see"
    >&2 echo "   https://github.com/eclipse/dash-licenses#automatic-ip-team-review-requests."
    >&2 echo " - Alternatively, review requests on the IP Team GitLab can be created manually, at"
    >&2 echo "   https://gitlab.eclipse.org/eclipsefdn/emo-team/iplab/-/issues."
    >&2 echo " - In case of a false positive, file an issue for the Eclipse Dash license check tool,"
    >&2 echo "   at https://github.com/eclipse/dash-licenses/issues."
    SCRIPT_EXIT_CODE=1
fi

# If the check failed, prepare a file to reduce the effort to create a GitLab issue.
MD_FILENAME=DEPENDENCIES.gitlab_issue_content.md
if [ $SCRIPT_EXIT_CODE -eq 0 ]; then
    rm -rf $MD_FILENAME
else
    echo "[Eclipse ESCET GitLab issue title]" > $MD_FILENAME
    echo >> $MD_FILENAME
    echo "Third party dependencies license check failure `date +%Y-%m-%d`" >> $MD_FILENAME
    echo >> $MD_FILENAME
    echo "[Eclipse ESCET GitLab issue description]" >> $MD_FILENAME
    if [ $ANY_DIFFS -ne 0 ]; then
        echo >> $MD_FILENAME
        echo "Differences found:" >> $MD_FILENAME
        echo "\`\`\`diff" >> $MD_FILENAME
        diff --strip-trailing-cr -u DEPENDENCIES.txt DEPENDENCIES.generated.processed.txt >> $MD_FILENAME
        echo "\`\`\`" >> $MD_FILENAME
    fi
    if [ $RESTRICTED -ne 1 ]; then
        echo >> $MD_FILENAME
        echo "Restricted dependencies found:" >> $MD_FILENAME
        echo "\`\`\`" >> $MD_FILENAME
        grep restricted DEPENDENCIES.generated.processed.txt >> $MD_FILENAME
        echo "\`\`\`" >> $MD_FILENAME
    fi

    >&2 echo
    >&2 echo "Please create an issue in the Eclipse ESCET GitLab at"
    >&2 echo "https://gitlab.eclipse.org/eclipse/escet/escet/-/issues"
    >&2 echo "to track the third party dependencies license check failure."
    >&2 echo "To create the issue with minimal effort, use the content from:"
    >&2 echo "$MD_FILENAME"
fi

# Indicate success.
if [ $SCRIPT_EXIT_CODE -eq 0 ]; then
    echo
    echo "SUCCESS: License check OK."
fi

# Cleanup.
rm target/dash/review-summary
rm DEPENDENCIES.generated.txt
if [ $ANY_DIFFS -eq 0 ]; then
    # No differences, so can remove generated file.
    rm DEPENDENCIES.generated.processed.txt
fi

# Exit with exit code 0 on success, and 1 on failure.
exit $SCRIPT_EXIT_CODE
