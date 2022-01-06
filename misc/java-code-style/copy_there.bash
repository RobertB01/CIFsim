#!/usr/bin/env bash

################################################################################
# Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

    # Filter to include only projects with a Java nature or documentation.
    grep -q "<nature>org.eclipse.jdt.core.javanature</nature>\|.documentation</name>" $p
    if test $? -ne 0
    then
        continue
    fi

    # Exclude product, except product.perspective.
    if [[ $p == ./product/org.eclipse.escet.product* ]]
    then
        if [[ $p != ./product/org.eclipse.escet.product.perspective* ]]
        then
            continue
        fi
    fi

    # Exclude third party.
    if [[ $p == ./thirdparty/* ]]
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

    # Show what we will update.
    TARGET_DIR=`dirname $p`
    echo $TARGET_DIR

    # Copy .checkstyle file.
    SOURCE_DIR=misc/java-code-style/
    cp -r $SOURCE_DIR/.checkstyle $TARGET_DIR

    # For Java projects, copy .settings folder.
    grep -q "<nature>org.eclipse.jdt.core.javanature</nature>" $p
    if test $? -eq 0
    then
        cp -r $SOURCE_DIR/.settings/ $TARGET_DIR
    fi

    set -e

    # Add builder and nature, if not yet present.
    misc/java-code-style/add_checkstyle_to_project_file.py $p
done
