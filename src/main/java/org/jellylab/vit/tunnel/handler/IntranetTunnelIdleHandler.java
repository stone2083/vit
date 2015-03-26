package org.jellylab.vit.tunnel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.IntranetTunnelConnection;

/**
 * @author jinli Mar 25, 2015
 */
public class IntranetTunnelIdleHandler extends ChannelInboundHandlerAdapter {

    private IntranetTunnelConnection conn;

    public IntranetTunnelIdleHandler(IntranetTunnelConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        conn.getChannel().close();
        conn.getIntranetTunnel().deleteIntranetTunnelConnection(conn);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        conn.getChannel().close();
        conn.getIntranetTunnel().deleteIntranetTunnelConnection(conn);
    }

}
