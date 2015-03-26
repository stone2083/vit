package org.jellylab.vit;

import io.netty.channel.Channel;

import org.jellylab.vit.tunnel.IntranetTunnel;

/**
 * @author jinli Mar 25, 2015
 */
public class IntranetTunnelConnection {

    private String eip;
    private int eport;

    private Channel channel;
    private IntranetTunnel intranetTunnel;

    public String getEip() {
        return eip;
    }

    public void setEip(String eip) {
        this.eip = eip;
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

    public IntranetTunnel getIntranetTunnel() {
        return intranetTunnel;
    }

    public void setIntranetTunnel(IntranetTunnel intranetTunnel) {
        this.intranetTunnel = intranetTunnel;
    }

}
