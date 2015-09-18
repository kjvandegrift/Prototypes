#!/bin/bash

gnome-terminal -t Client231 --geometry=117x20+1920+0   -e 'ssh server23 -t "cd ~/Deployed/Client231; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client232 --geometry=117x20+1920+400 -e 'ssh server23 -t "cd ~/Deployed/Client232; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client241 --geometry=117x20+1920+800 -e 'ssh server24 -t "cd ~/Deployed/Client241; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client242 --geometry=117x20+2890+0   -e 'ssh server24 -t "cd ~/Deployed/Client242; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client381 --geometry=117x20+2890+400 -e 'ssh server38 -t "cd ~/Deployed/Client381; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
gnome-terminal -t Client382 --geometry=117x20+2890+800 -e 'ssh server38 -t "cd ~/Deployed/Client382; java -jar SerializationClient-1.0-SNAPSHOT.jar server config.yml; bash"'
