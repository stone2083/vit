package org.jellylab.vit.agent;

import io.netty.channel.Channel;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentConnection {

    private AgentConnectionGroup group;
    private Channel channel;

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

}
