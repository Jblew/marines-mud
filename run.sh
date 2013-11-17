#!/bin/bash

jsvc -user muddeveloper -debug -outfile data/out.log -errfile data/err.log -pidfile data/pid.p -home /usr/lib/jvm/java-6-openjdk/ -Xmx10m -cp MarinesMUD4.jar marinesmud.Main production-mode
