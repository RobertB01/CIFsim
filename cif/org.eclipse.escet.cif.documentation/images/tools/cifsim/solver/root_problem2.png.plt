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

# Requires Gnuplot 4.4 or higher.

set terminal pngcairo size 500,300 font 'sans'
set output "root_problem2.png"

set key top left vertical

set style line 1 linecolor rgb '#dc3912' linetype 1 linewidth 2
set style line 2 linecolor rgb '#3366cc' linetype 1 linewidth 2
set style line 3 linecolor rgb '#109618' linetype 1 linewidth 2
set style line 4 linecolor rgb '#ff9900' linetype 1 linewidth 2
#set style line 5 linecolor rgb '#990099' linetype 1 linewidth 2
#set style line 6 linecolor rgb '#00cccc' linetype 1 linewidth 2

set style line 11 lc rgb '#808080' lt 1
set border 3 back ls 11
set border linewidth 1.5

set style line 12 lc rgb'#808080' lt 0 lw 1
set grid back ls 12
set grid back

set autoscale yfixmin
set autoscale yfixmax
set autoscale xfixmin
set autoscale xfixmax
set offsets 0, 0, graph 0.50, graph 0.05

set samples 2000

plot \
  "root_problem2.png.dat" using 1:($2) title "x(time)"  with linespoints ls 1, \
  "root_problem2.png.dat" using 1:($3) title "x'(time)" with linespoints ls 2, \
  (x >= 2.33) ? +1 : -1 title "first guard"             with lines       ls 3, \
  (x <= 2.34) ? +1 : -1 title "second guard"            with lines       ls 4
