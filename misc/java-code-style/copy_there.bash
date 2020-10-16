#!/usr/bin/env bash

################################################################################
# Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################# 

set -e -u

SCRIPT=`readlink -f $0`
SCRIPTPATH=`dirname $SCRIPT`
cd $SCRIPTPATH

cd ../..

PROJ_FILES=`find . -type f -name .project | sort`
for p in $PROJ_FILES
do
    set +e

    # Filter to include only projects with a Java nature.
    grep -q "<nature>org.eclipse.jdt.core.javanature</nature>" $p
    if test $? -ne 0
    then
        continue
    fi

    # Exclude products.
    if [[ $p == ./products/org.eclipse.escet.product* ]]
    then
        continue
    fi

    # Exclude metamodel projects (only generated code).
    if [[ $p == *.metamodel/.project ]]
    then
        continue
    fi

    # Exclude CIF simulator debug project (only generated code).
    if [[ $p == ./cif/org.eclipse.escet.cif.simulator.debug/.project ]]
    then
        continue
    fi

    set -e

    # Show what we will update.
    TARGET_DIR=`dirname $p`
    echo $TARGET_DIR

    # Copy files.
    SOURCE_DIR=misc/java-code-style/
    cp -r $SOURCE_DIR/.checkstyle $TARGET_DIR
    cp -r $SOURCE_DIR/.settings/ $TARGET_DIR

    # Add builder and nature, if not yet present.
    misc/java-code-style/add_checkstyle_to_project_file.py $p
done
