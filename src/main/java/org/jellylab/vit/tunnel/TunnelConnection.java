package org.jellylab.vit.tunnel;

import io.netty.channel.Channel;

/**
 * @author jinli Mar 25, 2015
 */
public class TunnelConnection {

    private String ehost;
    private int eport;

    private Channel channel;
    private Tunnel tunnel;

    public String getEhost() {
        return ehost;
    }

    public void setEhost(String ehost) {
        this.ehost = ehost;
    }

    public int getEport() {
        return eport;
    }

    public void setEport(int eport) {
        this.eport = eport;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Tunnel getTunnel() {
        return tunnel;
    }

    public void setTunnel(Tunnel tunnel) {
        this.tunnel = tunnel;
    }

}
