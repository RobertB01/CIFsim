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
set nokey
set xrange [0.5:6.5]
set yrange [0:0.25]
set xlabel "x"
set ylabel "uniform(int a, b)"
set label "uniform(1, 6)" at 3.5, 0.22
#set samples 2000
set output "disc_uni.svg"
set style data impulses
set pointsize 3
plot "disc_uni1.dat" using 1:2 with impulses linetype 1, \
     "disc_uni1.dat" using 1:2 with dots linetype 1, \
     "disc_uni2.dat" using 1:2 with points linetype 1 pt 6 ps 1
