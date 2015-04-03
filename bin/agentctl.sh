#!/bin/sh

BASE=$(dirname $0)/..

case $1 in
    start)
        if [ -z $2 ]; then
            conf="$BASE/conf/agent.conf"
        else
            conf=$2
        fi
        java -cp $(echo $BASE/libs/* | tr ' ' ':') -Dagent.conf=$conf org.jellylab.vit.agent.AgentServerMain
        ;;
    stop)
        echo 'stop'
        ;;
    *)
        echo 'usage:
        agentctl.sh start tunnel.conf
        agentctl.sh stop'
        ;;
esac
