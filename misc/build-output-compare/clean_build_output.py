#!/usr/bin/env python3

################################################################################
# Copyright (c) 2023 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
################################################################################# 

import itertools, re, sys

# Cleans Maven build output, stored in files.
# - The outputs may be from local builds or builds on Jenkins.
# - Some irrelevant differences are removed, to allow one to more easily spot relevant differences.
# - This script is meant for the build output of the Eclipse ESCET project. It may not suit other projects.

# Example usage:
# - python clean_build_output.py develop.txt branch.txt

# Clean each file.
for file in sys.argv[1:]:
    # Read lines.
    with open(file, 'r') as f:
        lines = f.readlines()

    # Remove timestamp prefix. E.g. '[2023-06-11T12:16:03.366Z] 0 issue(s) found...'.
    regex = r'^\[[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\.[0-9]{3}Z\]'
    replacement = r''
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Remove download progress. E.g. 'Fetching ...jar from https://download.eclipse.org/.../plugins/ (397.08kB)'.
    regex = r'^(\[INFO\] Fetching [^ ]+ from [^ ]+) \([^)]+\)$'
    replacement = r'\1'
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Replace time for running tests. E.g. '-[INFO] Tests run: 1, ..., Skipped: 0, Time elapsed: 0.084 s - in ...'.
    regex = r'Time elapsed: [0-9]+(\.[0-9]+)? s'
    replacement = r'Time elapsed: ??? s'
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Replace build name in paths. E.g. '[INFO] Building jar: /home/jenkins/agent/workspace/ESCET_build_MR-591/...'.
    regex = r'/ESCET_build_[^/]+/'
    replacement = r'/ESCET_BUILD_???/'
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Replace project OSGi versions messages. E.g. 'The project's OSGi version is 0.10.0.v20230611-085016-dev'.
    regex = r'(The project\'s OSGi version is [0-9]+\.[0-9]+\.[0-9]+\.v)[0-9]+-[0-9]+(-(dev|M[0-9]+|RC[0-9]+))?$'
    replacement = r'\1???-???\2'
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Replace installed product version. E.g. 'Installing org.eclipse.escet.product 0.10.0.v20230611-085016-dev.'.
    regex = r'(Installing org.eclipse.escet.product [0-9]+\.[0-9]+\.[0-9]+.v)[0-9]+-[0-9]+(-(dev|M[0-9]+|RC[0-9]+))?\.$'
    replacement = r'\1???-???\2.'
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Remove times from build summary. E.g. '[INFO] org.eclipse.escet.root .................. SUCCESS [  0.005 s]'.
    regex = r'\[ *[0-9]+\.[0-9]+ s\]'
    replacement = r''
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Remove times from build summary. E.g. '[INFO] org.eclipse.escet.chi.documentation ..... SUCCESS [01:29 min]'.
    regex = r'\[ *[0-9]+:[0-9]+ min\]'
    replacement = r''
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Remove times from operation completed messages. E.g. 'Operation completed in 17961 ms.'.
    regex = r'Operation completed in [0-9]+ ms'
    replacement = r'Operation completed in ??? ms'
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Remove timestamp when creating/signing DMG file.
    # E.g. '[INFO] [Sun Jun 11 09:06:52 UTC 2023] Creating and signing DMG file from '/home/jenkins/...'.
    regex = r'\[INFO\] \[[^\]]+\] (Creating and signing DMG file)'
    replacement = r'[INFO] [???] \1'
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Remove thread id and timestamp from Gdk-CRITICAL messages.
    # E.g. '(SWT:3732): Gdk-CRITICAL **: 08:59:09.791: gdk_threads_set_lock_functions: assertion '...' failed'.
    regex = r'\(SWT:[0-9]+\): Gdk-CRITICAL \*\*: [0-9]+:[0-9]+:[0-9]+\.[0-9]+: '
    replacement = r'(SWT:???): Gdk-CRITICAL **: ???: '
    lines = [re.sub(regex, replacement, line).strip() for line in lines]

    # Remove consecutive duplicate lines, such as for download progress messages.
    # E.g. '[INFO] Fetching ...jar from https://download.eclipse.org/.../plugins/'.
    lines = [x[0] for x in itertools.groupby(lines)]

    # Add newline after each line.
    lines = [l + '\n' for l in lines]

    # Write cleaned file.
    file2 = file + '.cleaned.txt'
    with open(file2, 'w') as f:
        for l in lines:
            f.write(l)

    # Print effect.
    print(f'Cleaned "{file}": result is in "{file2}".')
