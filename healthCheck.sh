#!/bin/bash
clear
curl http://10.1.10.51:8083/healthcheck?pretty=true
echo ""
curl http://10.1.10.23:8083/healthcheck?pretty=true
echo ""
curl http://10.1.10.24:8083/healthcheck?pretty=true
echo ""
curl http://10.1.10.51:9111/healthcheck?pretty=true
echo ""
curl http://10.1.10.51:9112/healthcheck?pretty=true
echo ""
curl http://10.1.10.23:9131/healthcheck?pretty=true
echo ""
curl http://10.1.10.23:9132/healthcheck?pretty=true
echo ""
curl http://10.1.10.24:9141/healthcheck?pretty=true
echo ""
curl http://10.1.10.24:9142/healthcheck?pretty=true
echo ""
