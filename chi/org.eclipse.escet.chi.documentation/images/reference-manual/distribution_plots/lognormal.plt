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
################################################################################

set terminal svg
#set grid back
#set xrange [0:20]
#set yrange [0:0.5]
set xlabel "x"
set ylabel "lognormal(real m, v2)"
#set samples 2000
set output "lognormal.svg"
set nokey
set label "lognormal(0.0, 1.0)" at 2.7, 0.17
set label "lognormal(0.0, 0.5)" at 1.7, 0.35
set label "lognormal(0.0, 0.25)" at 1.25, 1.2
set style data lines
plot "lognormal_0.0_1.0.dat" using 1:2 linetype 1, \
     "lognormal_0.0_0.5.dat" using 1:2 linetype 2, \
     "lognormal_0.0_0.25.dat" using 1:2 linetype 3

