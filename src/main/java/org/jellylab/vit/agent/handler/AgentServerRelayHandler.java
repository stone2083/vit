package org.jellylab.vit.agent.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.agent.AgentConnection;

/**
 * @author jinli Apr 3, 2015
 */
public class AgentServerRelayHandler extends ChannelInboundHandlerAdapter {

    private AgentConnection conn;

    public AgentServerRelayHandler(AgentConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        conn.getChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        conn.getServerChannel().close();
    }

}
