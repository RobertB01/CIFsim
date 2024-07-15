#!/usr/bin/env bash

################################################################################
# Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

# Set commands, if not yet defined.
# Set from outside script to override the default values.
GNUPLOT="${GNUPLOT:-gnuplot}"
INKSCAPE="${INKSCAPE:-inkscape}"
PDFLATEX="${PDFLATEX:-pdflatex}"
PDFCROP="${PDFCROP:-pdfcrop}"
CONVERT="${CONVERT:-convert}"

# .png.plt
for f in `find . -name "*.png.plt"`; do
    echo "Converting $f"
    cd `dirname $f`
    FILE=`basename $f`
    $GNUPLOT $FILE
    cd - > /dev/null
done

# .png.svg
for f in `find . -name "*.png.svg"`; do
    echo "Converting $f"
    FILE=${f//.png.svg/.png}
    $INKSCAPE --export-area-drawing --export-type="png" --export-filename=$FILE $FILE.svg
done

# .png.tex
for f in `find . -name "*.png.tex"`; do
    echo "Converting $f"
    cd `dirname $f`
    FILE=`basename ${f//.png.tex/.png}`
    $PDFLATEX -quiet $FILE.tex
    $PDFCROP $FILE.pdf $FILE.cropped.pdf > /dev/null
    $CONVERT -density 125 $FILE.cropped.pdf $FILE
    rm -f $FILE.pdf $FILE.log $FILE.aux $FILE.cropped.pdf
    cd - > /dev/null
done

# Done.
echo "Done!"
