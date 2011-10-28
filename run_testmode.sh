#!/bin/bash


script_exit_value=3

while true; do
    if [ "${script_exit_value}" == "3" ] ; then
        java -Xmx10m -jar MarinesMUD4.jar test-mode
        script_exit_value=$?
        echo " "
        echo " "
        echo "run.sh: exit code: ${script_exit_value}"

        if [ "${script_exit_value}" == "3" ] ; then
            echo " "
            echo " "
            echo "run.sh: restart"
            echo " "
            echo " "
        fi
    else
        echo "run.sh: Bye"
        break
    fi
done
