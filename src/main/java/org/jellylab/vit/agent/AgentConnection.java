package org.jellylab.vit.agent;

import io.netty.channel.Channel;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentConnection {

    private AgentConnectionGroup group;
    private Channel channel;
    private Channel serverChannel;

    public AgentConnectionGroup getGroup() {
        return group;
    }

    public void setGroup(AgentConnectionGroup group) {
        this.group = group;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

}
