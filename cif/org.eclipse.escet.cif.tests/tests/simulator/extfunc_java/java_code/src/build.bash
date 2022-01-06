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

set -e -u -x

# Clean up old class files.
CLS_FILES=`find ../bin -name '*.class'`
rm -f $CLS_FILES

# Compile source files.
JAVA_FILES=`find . -name '*.java'`
javac -d ../bin -source 11 -target 11 $JAVA_FILES

# List class files.
cd ../bin
CLS_FILES=`find -name '*.class'`
echo $CLS_FILES

# Package class files.
jar -cf test.jar $CLS_FILES

# Move JAR file.
mv test.jar ../jar
