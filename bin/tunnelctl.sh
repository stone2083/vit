#!/bin/sh

BASE=$(dirname $0)/..

case $1 in
    start)
        if [ -z $2 ]; then
            conf="$BASE/conf/tunnel.conf"
        else
            conf=$2
        fi
        java -cp $(echo $BASE/libs/* | tr ' ' ':') -Dtunnel.conf=$conf org.jellylab.vit.tunnel.TunnelServerMain
        ;;
    stop)
        echo 'stop'
        ;;
    *)
        echo 'usage:
        tunnelctl.sh start tunnel.conf
        tunnelctl.sh stop'
        ;;
esac
