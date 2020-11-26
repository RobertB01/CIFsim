#!/usr/bin/env sh

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
################################################################################

# Remove project exclusions once JavaBDD is in Orbit.
mvn -Dtycho.pomless.aggregator.names=releng,chi,cif,common,setext,tooldef,products -pl !cif/org.eclipse.escet.cif.datasynth,!cif/org.eclipse.escet.cif.datasynth.tests $*
