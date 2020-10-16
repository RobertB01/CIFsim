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

set terminal svg
#set grid back
#set xrange [0:20]
#set yrange [0:0.5]
set xlabel "x"
set ylabel "binomial(int n, real p)"
#set samples 2000
set output "binomial.svg"
set nokey
set label "binomial(20, 0.5)" at 2, 0.185
set label "binomial(20, 0.7)" at 16, 0.17
set label "binomial(40, 0.5)" at 24, 0.09
set style data impulses
set pointsize 1
plot "binomial_20_0.5.dat" using 1:2 with impulses linetype 1, \
     "binomial_20_0.5.dat" using 1:2 with points linetype 1, \
     "binomial_20_0.7.dat" using 1:2 with impulses linetype 2, \
     "binomial_20_0.7.dat" using 1:2 with points linetype 2, \
     "binomial_40_0.5.dat" using 1:2 with impulses linetype 3, \
     "binomial_40_0.5.dat" using 1:2 with points linetype 3
