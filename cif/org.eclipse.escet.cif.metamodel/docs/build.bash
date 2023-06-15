#!/usr/bin/env bash

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

set -e -u

cd $(dirname $(readlink -f $0))

DOCNAME=cif_ecore_doc

TODAY_DATE=`date +'%Y-%m-%d'`
sed -i'' -e "s/^\\\\date{Version .*}$/\\\\date{Version $TODAY_DATE}/g" $DOCNAME.tex

rm -f $DOCNAME.aux
rm -f $DOCNAME.bbl
rm -f $DOCNAME.blg
rm -f $DOCNAME.log
rm -f $DOCNAME.out
rm -f $DOCNAME.pdf
rm -f $DOCNAME.toc

pdflatex $DOCNAME.tex
bibtex $DOCNAME.aux
pdflatex $DOCNAME.tex
pdflatex $DOCNAME.tex

set +e

echo
echo "Summary:"
echo
grep "Warning: Citation" $DOCNAME.log
echo
grep "LaTeX Warning: There were undefined references" $DOCNAME.log

rm -f $DOCNAME.aux
rm -f $DOCNAME.bbl
rm -f $DOCNAME.blg
rm -f $DOCNAME.log
rm -f $DOCNAME.out
rm -f $DOCNAME.toc
