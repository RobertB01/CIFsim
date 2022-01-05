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
set xrange [0:5]
set yrange [0:0.9]
set xlabel "x"
set ylabel "triangle(real a, b, c)"
#set samples 2000
set output "triangle.svg"
set nokey
set label "triangle(1.0, 2.0, 4.0)" at 2.5, 0.55
set style data lines
plot "triangle_1_2_4.dat" using 1:2 linetype 1

