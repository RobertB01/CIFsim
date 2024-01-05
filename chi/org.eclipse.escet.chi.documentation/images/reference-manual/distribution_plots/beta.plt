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
################################################################################

set terminal svg
#set grid back
#set xrange [0:20]
#set yrange [0:0.5]
set xlabel "x"
set ylabel "beta(real p, q)"
#set samples 2000
set output "beta.svg"
set nokey
set label "beta(0.8, 0.5)" at 0.2, 0.8
set label "beta(1.5, 3.0)" at 0.15, 2
set label "beta(2.0, 2.0)" at 0.45, 1.6
set label "beta(5.0, 1.5)" at 0.57, 2.5
set style data lines
plot "beta_0.8_0.5.dat" using 1:2 linetype 1, \
     "beta_1.5_3.0.dat" using 1:2 linetype 2, \
     "beta_2.0_2.0.dat" using 1:2 linetype 3, \
     "beta_5.0_1.5.dat" using 1:2 linetype 4

