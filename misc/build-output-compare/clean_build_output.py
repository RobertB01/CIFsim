#!/usr/bin/env python3

################################################################################
# Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

# Configure replacements. Each entry consists of the regex to replace, and the replacement to replace it with.
replacements = [
    # Remove timestamp prefix. E.g. '[2023-06-11T12:16:03.366Z] 0 issue(s) found...'.
    (r'^\[[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\.[0-9]{3}Z\]',
     r''),

    # Remove download progress. E.g. 'Fetching ...jar from https://download.eclipse.org/.../plugins/ (397.08kB)'.
    (r'^(\[INFO\] Fetching [^ ]+ from [^ ]+) \([^)]+\)$',
     r'\1'),

    # Replace time for running tests. E.g. '-[INFO] Tests run: 1, ..., Skipped: 0, Time elapsed: 0.084 s - in ...'.
    (r'Time elapsed: [0-9]+(\.[0-9]+)? s',
     r'Time elapsed: ??? s'),

    # Replace build name in paths. E.g. 'Building jar: /home/jenkins/agent/workspace/build_758-upgrade-to-tycho-4-0-5/...'.
    (r'/home/jenkins/agent/workspace/[^/]+/',
      '/home/jenkins/agent/workspace/ESCET_BUILD_???/'),

    # Replace project OSGi versions messages. E.g. 'The project's OSGi version is 0.10.0.v20230611-085016-dev'.
    (r'(The project\'s OSGi version is [0-9]+\.[0-9]+\.[0-9]+\.v)[0-9]+-[0-9]+(-(dev|nightly|M[0-9]+|RC[0-9]+))?$',
     r'\1???-???-???'),

    # Replace installed product version. E.g. 'Installing org.eclipse.escet.product 0.10.0.v20230611-085016-dev.'.
    (r'(Installing org.eclipse.escet.product [0-9]+\.[0-9]+\.[0-9]+.v)[0-9]+-[0-9]+(-(dev|nightly|M[0-9]+|RC[0-9]+))?\.$',
     r'\1???-???-???.'),

    # Remove times from build summary. E.g. '[INFO] org.eclipse.escet.root .................. SUCCESS [  0.005 s]'.
    (r'\[ *[0-9]+\.[0-9]+ s\]',
     r''),

    # Remove times from build summary. E.g. '[INFO] org.eclipse.escet.chi.documentation ..... SUCCESS [01:29 min]'.
    (r'\[ *[0-9]+:[0-9]+ min\]',
     r''),

    # Remove times from operation completed messages. E.g. 'Operation completed in 17961 ms.'.
    (r'Operation completed in [0-9]+ ms',
     r'Operation completed in ??? ms'),

    # Remove timestamp when creating/signing DMG file.
    # E.g. '[INFO] [Sun Jun 11 09:06:52 UTC 2023] Creating and signing DMG file from '/home/jenkins/...'.
    (r'\[INFO\] \[[^\]]+\] (Creating and signing DMG file)',
     r'[INFO] [???] \1'),

    # Remove thread id and timestamp from Gdk-CRITICAL messages.
    # E.g. '(SWT:3732): Gdk-CRITICAL **: 08:59:09.791: gdk_threads_set_lock_functions: assertion '...' failed'.
    (r'\(SWT:[0-9]+\): Gdk-CRITICAL \*\*: [0-9]+:[0-9]+:[0-9]+\.[0-9]+: ',
     r'(SWT:???): Gdk-CRITICAL **: ???: '),
]

# Compile regexs.
replacements = [(re.compile(regex), replacement) for (regex, replacement) in replacements]

# Line sorting functions.
def sort_modules(lines):
    # Sort output of building different modules, on ascending module names:
    # - Allows to more easily compare output in case the order has changed.
    # - The output per building a module is not sorted, but kept as is.
    # - As each module is numbered, and the numbers are kept, reorderings can still be detected in the sorted output.
    #
    # E.g.:
    # [INFO] ----------------------< org.eclipse.escet:common >----------------------
    # [INFO] Building [aggregator] common 3.0.0-SNAPSHOT                      [4/113]
    # [INFO]   from common/.polyglot.pom.tycho
    # [INFO] --------------------------------[ pom ]---------------------------------
    #
    # After the modules are built, the reactor summary is shown. E.g.:
    #
    # [INFO] ------------------------------------------------------------------------
    # [INFO] Reactor Summary for org.eclipse.escet.root 3.0.0-SNAPSHOT:

    # Create regular expressions to match certain lines.
    module_line1_regex = re.compile('^\[INFO\] -+< ([a-zA-Z0-9_.:]+) >-+$')
    module_line2_regex = re.compile('^\[INFO\] Building .* \[(\d+)/(\d+)\]+$')
    post_line1_regex = re.compile('^\[INFO\] ------------------------------------------------------------------------$')
    post_line2_regex = re.compile('^\[INFO\] Reactor Summary for .*:$')

    # Initialize output: pre lines (before module output), module lines (name to lines for a module), and post lines
    # (after module output).
    pre_lines = []
    module_lines = dict()
    post_lines = []

    # Get pre lines.
    idx = 0
    while True:
        line = lines[idx]
        match = re.match(module_line1_regex, line)
        if match is None:
            pre_lines.append(line)
            idx += 1
        else:
            break

    # Get module lines.
    cur_module_nr = 0
    nr_of_modules = None
    while True:
        # Get module name from first line.
        line = lines[idx]
        idx += 1

        match = re.match(module_line1_regex, line)
        assert match is not None, line

        module_name = match.group(1)

        # Add entry for module lines of this module, adding the first line.
        this_module_lines = []
        module_lines[module_name] = this_module_lines
        this_module_lines.append(line)

        # Get module number and number of modules, from second line.
        line = lines[idx]
        idx += 1
        this_module_lines.append(line)

        match = re.match(module_line2_regex, line)
        assert match is not None, line

        this_module_nr = match.group(1)
        this_nr_of_modules = match.group(2)

        # Check module number.
        cur_module_nr += 1
        assert int(this_module_nr) == cur_module_nr

        # Update/check number of modules.
        if nr_of_modules is None:
            nr_of_modules = int(this_nr_of_modules)
        else:
            assert nr_of_modules == int(this_nr_of_modules) 

        # Get remaining lines of the module.
        while idx < len(lines):
            line = lines[idx]
            match1 = re.match(module_line1_regex, line)
            match2 = re.match(post_line1_regex, line)
            if match1 is not None:
                break
            elif match2 is not None:
                # Make sure this is really the reactor summary.
                assert re.match(post_line2_regex, lines[idx + 1]) is not None

                # Check that enough modules have been found.
                assert nr_of_modules is not None
                assert nr_of_modules == len(module_lines)

                # Past the modules output. Determine post lines.
                post_lines = lines[idx:]

                # Returned reorder lines.
                new_lines = pre_lines
                for name, lines in sorted(module_lines.items()):
                    new_lines += lines
                new_lines += post_lines
                return new_lines
            else:
                this_module_lines.append(line)
                idx += 1

def sort_copy_lines(lines):
    line_prefix = '[INFO]      [copy] Copying /home/jenkins/'

    new_lines = []
    idx = 0
    while idx < len(lines):
        line = lines[idx]
        idx += 1

        if not line.startswith(line_prefix):
            new_lines.append(line)
            continue

        lines_to_sort = [line]
        while idx < len(lines):
            line = lines[idx]
            if line.startswith(line_prefix):
                lines_to_sort.append(line)
                idx += 1
            else:
                break
        new_lines += sorted(lines_to_sort)

    return new_lines

# Clean each file.
for file in sys.argv[1:]:
    # Read lines.
    with open(file, 'r') as f:
        lines = f.readlines()

    # Apply the replacements.
    for regex, replacement in replacements:
        lines = [regex.sub(replacement, line).strip() for line in lines]

    # Remove consecutive duplicate lines, such as for download progress messages.
    # E.g. '[INFO] Fetching ...jar from https://download.eclipse.org/.../plugins/'.
    lines = [x[0] for x in itertools.groupby(lines)]

    # Sort output of building different modules, on ascending module names.
    lines = sort_modules(lines)

    # Sort blocks of '[copy] Copying /home/jenkins/... lines.
    lines = sort_copy_lines(lines)

    # Write cleaned file.
    file2 = file + '.cleaned.txt'
    with open(file2, 'w') as f:
        for l in lines:
            f.write(l)
            f.write('\n')

    # Print effect.
    print(f'Cleaned "{file}": result is in "{file2}".')
