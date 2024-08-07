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
################################################################################

for f in `find -name "*.mcrl2" -type f`
do
    echo $f
    mcrl22lps $f -o $f.lps
    lps2lts $f.lps $f.aut
    rm $f.lps
done
