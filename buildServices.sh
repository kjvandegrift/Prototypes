#!/bin/bash

gnome-terminal -t Serialization --geometry=237x32+1920+0 --working-directory=/home/kvandegrift/Prototypes/Serialization  -x sh -c "mvn clean package; exec bash"
gnome-terminal -t Client --geometry=237x32+1920+600 --working-directory=/home/kvandegrift/Prototypes/Client -x sh -c "mvn clean package; exec bash"
