#!/usr/bin/env bash

################################################################################
# Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################# 

# This script copies the Java code related settings from a specific project to this folder, to be subsequently applied
# to all relevant projects using the 'copy_there.bash' script.
#
# Usage: ./copy_here.bash <path_to_project>
# E.g.:  ./copy_here.bash ../../common/org.eclipse.escet.common.java
#
# See the Eclipse ESCET development documentation for more information.

set -e -u

SCRIPT=`readlink -f $0`
SCRIPTPATH=`dirname $SCRIPT`

cp $1/.checkstyle $SCRIPTPATH
cp $1/.settings/* $SCRIPTPATH/.settings
