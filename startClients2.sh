#!/bin/bash

gnome-terminal -t Client391 --geometry=117x20+1920+0   -e 'ssh server39 -t "cd ~/Deployed/Client391; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client392 --geometry=117x20+1920+400 -e 'ssh server39 -t "cd ~/Deployed/Client392; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client411 --geometry=117x20+1920+800 -e 'ssh server41 -t "cd ~/Deployed/Client411; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client412 --geometry=117x20+2890+0   -e 'ssh server41 -t "cd ~/Deployed/Client412; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client421 --geometry=117x20+2890+400 -e 'ssh server42 -t "cd ~/Deployed/Client421; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client422 --geometry=117x20+2890+800 -e 'ssh server42 -t "cd ~/Deployed/Client422; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
