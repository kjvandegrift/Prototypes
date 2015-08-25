#!/bin/bash
clear
rpmbuild --bb /home/kvandegrift/rpmbuild/SPECS/serialization.spec
if [ -f /home/kvandegrift/rpmbuild/RPMS/x86_64/Serialization-0.1-1.x86_64.rpm ]; then
  cp -f /home/kvandegrift/rpmbuild/RPMS/x86_64/Serialization-0.1-1.x86_64.rpm /home/kvandegrift
  scp /home/kvandegrift/rpmbuild/RPMS/x86_64/Serialization-0.1-1.x86_64.rpm kvandegrift@server23:/home/kvandegrift
  scp /home/kvandegrift/rpmbuild/RPMS/x86_64/Serialization-0.1-1.x86_64.rpm kvandegrift@server24:/home/kvandegrift
fi

