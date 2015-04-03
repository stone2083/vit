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
        pid=$(jps | grep -i TunnelServerMain | awk '{print $1}')
        kill -1 $pid
        ;;
    *)
        echo 'usage:
        agentctl.sh start tunnel.conf
        agentctl.sh stop'
        ;;
esac
