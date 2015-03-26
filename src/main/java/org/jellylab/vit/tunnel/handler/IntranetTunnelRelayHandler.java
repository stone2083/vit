package org.jellylab.vit.tunnel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.SocksConnection;

/**
 * @author jinli Mar 25, 2015
 */
public class IntranetTunnelRelayHandler extends ChannelInboundHandlerAdapter {

    private SocksConnection conn;

    public IntranetTunnelRelayHandler(SocksConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        conn.getChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (conn.getChannel().isActive()) {
            conn.getChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        conn.getChannel().close();
    }

}
