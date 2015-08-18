#!/bin/bash

if [ -f results_51_9000.log ]; then
    rm results_51_9000.log
fi
if [ -f results_23_9000.log ]; then
    rm results_23_9000.log 
fi
if [ -f results_24_9000.log ]; then
    rm results_24_9000.log
fi
if [ -f results_51_9002.log ]; then
    rm results_51_9002.log
fi
if [ -f results_23_9002.log ]; then
    rm results_23_9002.log 
fi
if [ -f results_24_9002.log ]; then
    rm results_24_9002.log
fi
if [ -f results_all.log ]; then
    rm results_all.log
fi

curl http://10.1.10.51:9000/startSimulator >> results_51_9000.log &
#curl http://10.1.10.23:9000/startSimulator >> results_23_9000.log &
#curl http://10.1.10.24:9000/startSimulator >> results_24_9000.log &
#curl http://10.1.10.51:9002/startSimulator >> results_51_9002.log &
#curl http://10.1.10.23:9002/startSimulator >> results_23_9002.log &
#curl http://10.1.10.24:9002/startSimulator >> results_24_9002.log &

wait

cat results_{51_9000,23_9000,24_9000,51_9002,23_9002,24_9002}.log > results_all.log
