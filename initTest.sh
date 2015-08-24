#!/bin/bash
clear
#(
curl http://10.1.10.51:8083/healthcheck?pretty=true
echo ""
#curl http://10.1.10.23:8083/healthcheck?pretty=true
#echo ""
#curl http://10.1.10.24:8083/healthcheck?pretty=true
#echo ""
#curl http://10.1.10.51:9001/healthcheck?pretty=true
#echo ""
#curl http://10.1.10.23:9001/healthcheck?pretty=true
#echo ""
#curl http://10.1.10.24:9001/healthcheck?pretty=true
#echo ""
curl -X POST http://10.1.10.51:8082/sequences/alpha
echo ""
curl -X POST http://10.1.10.51:8082/sequences/digit
echo ""
#:) | less
