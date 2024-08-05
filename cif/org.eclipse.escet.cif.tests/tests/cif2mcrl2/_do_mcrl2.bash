#!/usr/bin/env bash

for f in `find -name "*.mcrl2" -type f`
do
    echo $f
    mcrl22lps $f -o $f.lps
    lps2lts $f.lps $f.aut
    rm $f.lps
done
