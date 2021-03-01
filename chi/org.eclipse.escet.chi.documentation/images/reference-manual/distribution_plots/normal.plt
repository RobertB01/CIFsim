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
set xrange [0:10]
set yrange [0:0.5]
set xlabel "x"
set ylabel "normal(real m, v2)"
#set samples 2000
set output "normal.svg"
set nokey
set label "normal(3.0, 1.0)" at 4.2, 0.25
set label "normal(5.0, 2.0)" at 6.5, 0.17
set style data lines
plot "normal_3_1.dat" using 1:2 linetype 1, \
     "normal_5_2.dat" using 1:2 linetype 2

