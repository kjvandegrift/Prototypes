#!/bin/bash

if [ -f results_23_1.log ]; then
    rm results_23_1.log 
fi
if [ -f results_23_2.log ]; then
    rm results_23_2.log 
fi
if [ -f results_24_1.log ]; then
    rm results_24_1.log
fi
if [ -f results_24_2.log ]; then
    rm results_24_2.log
fi
if [ -f results_38_1.log ]; then
    rm results_38_1.log 
fi
if [ -f results_38_2.log ]; then
    rm results_38_2.log 
fi
if [ -f results_39_1.log ]; then
    rm results_39_1.log
fi
if [ -f results_39_2.log ]; then
    rm results_39_2.log
fi
if [ -f results_41_1.log ]; then
    rm results_41_1.log 
fi
if [ -f results_41_2.log ]; then
    rm results_41_2.log 
fi
if [ -f results_42_1.log ]; then
    rm results_42_1.log
fi
if [ -f results_42_2.log ]; then
    rm results_42_2.log
fi
if [ -f results_all.log ]; then
    rm results_all.log
fi
curl http://10.1.10.23:6231/startSimulator >> results_23_1.log &
curl http://10.1.10.23:6232/startSimulator >> results_23_2.log &
curl http://10.1.10.24:6241/startSimulator >> results_24_1.log &
curl http://10.1.10.24:6242/startSimulator >> results_24_2.log &
curl http://10.1.10.38:6381/startSimulator >> results_38_1.log &
curl http://10.1.10.38:6382/startSimulator >> results_38_2.log &
curl http://10.1.10.39:6391/startSimulator >> results_39_1.log &
curl http://10.1.10.39:6392/startSimulator >> results_39_2.log &
curl http://10.1.10.41:6411/startSimulator >> results_41_1.log &
curl http://10.1.10.41:6412/startSimulator >> results_41_2.log &
curl http://10.1.10.42:6421/startSimulator >> results_42_1.log &
curl http://10.1.10.42:6422/startSimulator >> results_42_2.log &

wait

cat results_{23_1,23_2,24_1,24_2,38_1,38_2,39_1,39_2,41_1,41_2,42_1,42_2}.log > results_all.log
