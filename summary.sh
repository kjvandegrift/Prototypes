#!/bin/bash
if [ -f results_23_1.log ]; then
    wc -l results_23_1.log
    sort results_23_1.log | uniq -cd
fi
if [ -f results_23_2.log ]; then
    wc -l results_23_2.log
    sort results_23_2.log | uniq -cd
fi
if [ -f results_24_1.log ]; then
    wc -l results_24_1.log
    sort results_24_1.log | uniq -cd
fi
if [ -f results_24_2.log ]; then
    wc -l results_24_2.log
    sort results_24_2.log | uniq -cd
fi
if [ -f results_38_1.log ]; then
    wc -l results_38_1.log
    sort results_38_1.log | uniq -cd
fi
if [ -f results_38_2.log ]; then
    wc -l results_38_2.log
    sort results_38_2.log | uniq -cd
fi
if [ -f results_39_1.log ]; then
    wc -l results_39_1.log
    sort results_39_1.log | uniq -cd
fi
if [ -f results_39_2.log ]; then
    wc -l results_39_2.log
    sort results_39_2.log | uniq -cd
fi
if [ -f results_41_1.log ]; then
    wc -l results_41_1.log
    sort results_41_1.log | uniq -cd
fi
if [ -f results_41_2.log ]; then
    wc -l results_41_2.log
    sort results_41_2.log | uniq -cd
fi
if [ -f results_42_1.log ]; then
    wc -l results_42_1.log
    sort results_42_1.log | uniq -cd
fi
if [ -f results_42_2.log ]; then
    wc -l results_42_2.log
    sort results_42_2.log | uniq -cd
fi
if [ -f results_all.log ]; then
    wc -l results_all.log
    sort results_all.log | uniq -cd
fi 
echo ""
