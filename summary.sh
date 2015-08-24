#!/bin/bash
if [ -f results_51_9000.log ]; then
    wc -l results_51_9000.log
    sort results_51_9000.log | uniq -cd
fi
if [ -f results_23_9000.log ]; then
    wc -l results_23_9000.log
    sort results_23_9000.log | uniq -cd
fi
if [ -f results_24_9000.log ]; then
    wc -l results_24_9000.log
    sort results_24_9000.log | uniq -cd
fi
if [ -f results_all.log ]; then
    wc -l results_all.log
    sort results_all.log | uniq -cd
fi 
echo ""
