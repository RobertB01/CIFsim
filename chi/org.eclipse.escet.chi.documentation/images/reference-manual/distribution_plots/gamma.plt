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
set nokey
#set grid back
#set xrange [0:20]
#set yrange [0:0.5]
set xlabel "x"
set ylabel "gamma(real a, b)"
#set samples 2000
set output "gamma.svg"
set label "gamma(1.0, 2.0)" at 0.8, 0.42
set label "gamma(3.0, 2.0)" at 6, 0.13
set label "gamma(6.0, 2.0)" at 13.0, 0.09
set label "gamma(6.0, 0.5)" at 3.8, 0.27
set style data lines
plot "gamma_1.0_2.0.dat" using 1:2 linetype 1, \
     "gamma_3.0_2.0.dat" using 1:2 linetype 2, \
     "gamma_6.0_2.0.dat" using 1:2 linetype 3, \
     "gamma_6.0_0.5.dat" using 1:2 linetype 4
