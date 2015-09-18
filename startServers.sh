#!/bin/bash

gnome-terminal -t Server23 --geometry=117x20+1920+0   -e 'ssh server23 -t "cd ~/Deployed/Server23; java -jar Serialization-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Server24 --geometry=117x20+1920+400 -e 'ssh server24 -t "cd ~/Deployed/Server24; java -jar Serialization-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Server38 --geometry=117x20+1920+800 -e 'ssh server38 -t "cd ~/Deployed/Server38; java -jar Serialization-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Server39 --geometry=117x20+2890+0   -e 'ssh server39 -t "cd ~/Deployed/Server39; java -jar Serialization-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Server41 --geometry=117x20+2890+400 -e 'ssh server41 -t "cd ~/Deployed/Server41; java -jar Serialization-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Server42 --geometry=117x20+2890+800 -e 'ssh server42 -t "cd ~/Deployed/Server42; java -jar Serialization-1.0-SNAPSHOT.jar server config.yml; bash"'
