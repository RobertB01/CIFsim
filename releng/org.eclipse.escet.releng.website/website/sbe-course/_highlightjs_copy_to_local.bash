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
# reuses those assets. While viewing the SBE course locally, these assets are not available. Run this script to make
# the files locally available, for editing/viewing/debugging purposes.

set -e -u

cd $(dirname $(readlink -f $0))

cp -r ../../../org.eclipse.escet.releng.configuration/highlightjs-assets/third-party/* ../assets/
cp -r ../../../org.eclipse.escet.releng.configuration/highlightjs-assets/escet/* ../assets/
