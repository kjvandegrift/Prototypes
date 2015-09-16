#!/bin/bash
clear
curl -X POST http://10.1.10.23:8000/sequences/alpha/ZZZ
echo ""
curl -X POST http://10.1.10.23:8000/sequences/digit/999
echo ""
