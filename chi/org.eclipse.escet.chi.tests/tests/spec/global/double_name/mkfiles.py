#!/usr/bin/env python

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
#################################################################################

def write(name, text):
    fp = open(name, 'w')
    fp.write(text)
    fp.close()

c1 = 'const int M = 5;'
e1 = 'enum M = { A, B, C };'
e2 = 'enum X = { D, M, F };'
f1 = 'func string M(): return "1"; end'
f2 = 'func string M(real r): return "1"; end'
m1 = 'model M(): pass; end'
m2 = 'model M(int x): pass; end'
p1 = 'proc M(): pass; end'
p2 = 'proc M(int x): pass; end'
t1 = 'type M = list int;'


names = {'c1' : c1,
         'e1' : e1, 'e2' : e2,
         'f1' : f1, 'f2' : f2,
         'm1' : m1, 'm2' : m2,
         'p1' : p1, 'p2' : p2,
         't1' : t1,}

for xn, xv in names.items():
    for yn, yv in names.items():
        name = xn + yn + '.chi'
        text = xv + '\n' + yv + '\n'
        write(name, text)

write('e3.chi', 'enum M = { A, M, C };')
