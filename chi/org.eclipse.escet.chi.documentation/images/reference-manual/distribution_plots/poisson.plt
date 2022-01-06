################################################################################
# Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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
#set xrange [0:10]
set yrange [0:0.22]
set xlabel "x"
set ylabel "poisson(real lambda)"
#set samples 2000
set output "poisson.svg"
set nokey
set label "poisson(4.0)" at 4.4, 0.18
set label "poisson(10.0)" at 11.5, 0.11
set style data impulses
plot "poisson_4.0.dat" using 1:2 with impulses linetype 1, \
     "poisson_4.0.dat" using 1:2 with points linetype 1, \
     "poisson_10.0.dat" using 1:2 with impulses linetype 2, \
     "poisson_10.0.dat" using 1:2 with points linetype 2

