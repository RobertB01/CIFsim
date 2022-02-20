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
#set xrange [0:20]
#set yrange [0:0.5]
set xlabel "x"
set ylabel "exponential(real m)"
#set samples 2000
set output "exponential.svg"
set nokey
set label "exponential(0.5)" at 0.2, 1.5
set label "exponential(1.0)" at 0.9, 0.45
set label "exponential(1.5)" at 2.5, 0.2
set style data lines
plot "exponential_0.5.dat" using 1:2 linetype 1, \
     "exponential_1.0.dat" using 1:2 linetype 2, \
     "exponential_1.5.dat" using 1:2 linetype 3

