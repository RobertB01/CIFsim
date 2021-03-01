#!/usr/bin/env bash

################################################################################
# Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

grep -e "^\\\\citem" ../../../../../../../org.eclipse.escet.cif.metamodel/docs/cif_ecore_doc_details.tex | sed -r "s|\\\\citem(nf)?\{([^}]+)\}|\2|g" | sort > tmp.constraints_ecore.tmp
grep "^    // " ErrMsg.java | sed -r "s|^[^a-zA-Z]+||" | grep -v " " | sort -u > tmp.constraints_errmsg.tmp
diff -u tmp.constraints_ecore.tmp tmp.constraints_errmsg.tmp
echo "No differences found!"
rm tmp.constraints_ecore.tmp tmp.constraints_errmsg.tmp
