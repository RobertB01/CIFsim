#!/usr/bin/env python

################################################################################
# Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################# 

# This Python script is NOT intended to be used directly, but only by the 'copy_there.bash' script.

import sys

# Read input file. Path provided as first command line argument.
with open(sys.argv[1]) as f:
    lines = f.readlines()

# See if builder needs to be added.
builder_idx = -1
try:
    idx = lines.index('\t\t\t<name>net.sf.eclipsecs.core.CheckstyleBuilder</name>\n')
except ValueError:
    # Add it after the last build command.
    builder_idx = len(lines) - lines[::-1].index('\t\t</buildCommand>\n') - 1

# See if nature needs to be added.
nature_idx = -1
try:
    idx = lines.index('\t\t<nature>net.sf.eclipsecs.core.CheckstyleNature</nature>\n')
except ValueError:
    # Add it as the last nature.
    nature_idx = len(lines) - lines[::-1].index('\t</natures>\n') - 2

# Overwrite file if we need to add something.
if builder_idx != -1 or nature_idx != -1:
    with open(sys.argv[1], 'w') as f:
        # Write all lines.
        for i in range(len(lines)):
            f.write(lines[i])

            # Add builder, if needed.
            if i == builder_idx:
                f.write('\t\t<buildCommand>\n')
                f.write('\t\t\t<name>net.sf.eclipsecs.core.CheckstyleBuilder</name>\n')
                f.write('\t\t\t<arguments>\n')
                f.write('\t\t\t</arguments>\n')
                f.write('\t\t</buildCommand>\n')

            # Add nature, if needed.
            if i == nature_idx:
                f.write('\t\t<nature>net.sf.eclipsecs.core.CheckstyleNature</nature>\n')
