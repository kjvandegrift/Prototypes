#!/bin/bash
if [ -f results_51_9011.log ]; then
    wc -l results_51_9011.log
    sort results_51_9011.log | uniq -cd
fi
if [ -f results_51_9012.log ]; then
    wc -l results_51_9012.log
    sort results_51_9012.log | uniq -cd
fi
if [ -f results_23_9031.log ]; then
    wc -l results_23_9031.log
    sort results_23_9031.log | uniq -cd
fi
if [ -f results_23_9032.log ]; then
    wc -l results_23_9032.log
    sort results_23_9032.log | uniq -cd
fi
if [ -f results_24_9041.log ]; then
    wc -l results_24_9041.log
    sort results_24_9041.log | uniq -cd
fi
if [ -f results_24_9042.log ]; then
    wc -l results_24_9042.log
    sort results_24_9042.log | uniq -cd
fi
if [ -f results_all.log ]; then
    wc -l results_all.log
    sort results_all.log | uniq -cd
fi 
echo ""
