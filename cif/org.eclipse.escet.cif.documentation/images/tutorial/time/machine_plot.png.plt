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

# Requires Gnuplot 4.4 or higher.

set terminal pngcairo size 600,300 font 'sans'
set output "machine_plot.png"

set key outside horizontal bottom center

set style line 1 linecolor rgb '#dc3912' linetype 1 linewidth 2
set style line 2 linecolor rgb '#3366cc' linetype 1 linewidth 2
set style line 3 linecolor rgb '#109618' linetype 1 linewidth 2
set style line 4 linecolor rgb '#ff9900' linetype 1 linewidth 2
set style line 5 linecolor rgb '#990099' linetype 1 linewidth 2
set style line 6 linecolor rgb '#00cccc' linetype 1 linewidth 2

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
set offsets 0, 0, graph 0.05, graph 0.05

plot \
  "machine_plot.png.dat" using 1:($1) title "time(time)" with lines ls 1, \
  "machine_plot.png.dat" using 1:($2) title "t(time)"    with lines ls 2, \
  "machine_plot.png.dat" using 1:($3) title "t'(time)"   with lines ls 3
