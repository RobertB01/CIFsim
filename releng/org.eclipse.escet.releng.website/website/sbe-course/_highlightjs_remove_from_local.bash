#!/usr/bin/env bash

################################################################################
# Copyright (c) 2024 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the terms
# of the MIT License which is available at https://opensource.org/licenses/MIT
#
# SPDX-License-Identifier: MIT
#################################################################################

# During the build, the highlight.js assets are part of the AsciiDoctor-generated documentation, and the SBE course
# reuses those assets. While viewing the SBE course locally, these assets are not available. Once copied locally, a
# local build will fail, due to the files already being present. Run this script to remove the local copies of the
# highlight.js asset files.

set -e -u

cd $(dirname $(readlink -f $0))

cd ../assets
for f in `git ls-files --others --ignored --exclude-standard`; do
    rm $f
done
rm -d languages/
rm -d styles/
