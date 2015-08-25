#!/bin/bash

if [ -f results_51_9011.log ]; then
    rm results_51_9011.log
fi
if [ -f results_51_9012.log ]; then
    rm results_51_9012.log
fi
if [ -f results_23_9031.log ]; then
    rm results_23_9031.log 
fi
if [ -f results_23_9032.log ]; then
    rm results_23_9032.log 
fi
if [ -f results_24_9041.log ]; then
    rm results_24_9041.log
fi
if [ -f results_24_9042.log ]; then
    rm results_24_9042.log
fi
if [ -f results_all.log ]; then
    rm results_all.log
fi
curl http://10.1.10.51:9011/startSimulator >> results_51_9011.log &
curl http://10.1.10.51:9012/startSimulator >> results_51_9012.log &
curl http://10.1.10.23:9031/startSimulator >> results_23_9031.log &
#curl http://10.1.10.23:9032/startSimulator >> results_23_9032.log &
#curl http://10.1.10.24:9041/startSimulator >> results_24_9041.log &
curl http://10.1.10.24:9042/startSimulator >> results_24_9042.log &

wait

cat results_{51_9011,24_9042,51_9012,23_9031}.log > results_all.log
#cat results_51_9000.log > results_all.log
