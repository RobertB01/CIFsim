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
#set xrange [0:10]
set yrange [0:2.5]
set xlabel "x"
set ylabel "weibull(real a, b)"
#set samples 2000
set output "weibull.svg"
set nokey
set label "weibull(0.5, 1.0)" at 0.1, 2.2
set label "weibull(1.0, 1.0)" at 2.2, 0.2
set label "weibull(1.5, 1.0)" at 1.4, 0.45
set label "weibull(5.0, 1.0)" at 1.15, 1.4
set style data lines
plot "weibull_0.5_1.0.dat" using 1:2 linetype 1, \
     "weibull_1.0_1.0.dat" using 1:2 linetype 2, \
     "weibull_1.5_1.0.dat" using 1:2 linetype 3, \
     "weibull_5.0_1.0.dat" using 1:2 linetype 4

