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
#
# Makefile for creating the reference manual rail road images
#
LATEX=latex
DVIPS=dvips
RAIL=rail
CROP=bbox_add.pl
EPSPNG=eps2png

NONSOURCE=picture.tex preamble.tex postamble.tex
SOURCE=$(filter-out $(NONSOURCE),$(wildcard *.tex))
DEST=$(SOURCE:.tex=.png)

.phony: images clean

images: $(DEST)

# eps2png: -t=transparent, -f=force writing, -d=dpi
%.png: %.tex
	cat preamble.tex $< postamble.tex > picture.tex
	$(LATEX) picture.tex
	$(RAIL) picture
	$(LATEX) picture.tex
	$(DVIPS) -E -o picture.eps picture.dvi
	$(CROP) picture.eps
	$(EPSPNG) -d 120 -f -t picture.eps
	mv picture.png $@

clean:
	for i in $(DEST) picture.aux picture.dvi picture.eps picture.log picture.tex;\
	do if test -f $$i;\
	   then rm $$i;\
	   fi;\
	done
