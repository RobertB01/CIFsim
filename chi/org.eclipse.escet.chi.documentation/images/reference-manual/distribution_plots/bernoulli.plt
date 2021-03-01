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
################################################################################

set terminal svg
#set grid back
set nokey
set xrange [-0.2:1.2]
set yrange [0:1]
set xlabel "x"
set ylabel "bernoulli(real p)"
set label "bernoulli(0.69)" at 0.7, 0.5
#set samples 2000
set xtics ("false" 0, "true" 1)
set output "bernoulli.svg"
set style data impulses
set pointsize 1
plot "bernoulli.dat" using 1:2 with impulses linetype 1, \
     "bernoulli.dat" using 1:2 with points linetype 1
