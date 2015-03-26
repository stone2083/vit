package org.jellylab.vit.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.IntranetTunnelConnection;
import org.jellylab.vit.tunnel.handler.IntranetTunnelRelayHandler;

/**
 * @author jinli Mar 25, 2015
 */
public class SocksRelayHandler extends ChannelInboundHandlerAdapter {

    private IntranetTunnelConnection conn;

    public SocksRelayHandler(IntranetTunnelConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        conn.getChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (conn.getChannel().isActive()) {
            conn.getChannel().pipeline().remove(IntranetTunnelRelayHandler.class);
            conn.getIntranetTunnel().returnIntranetTunnelConnection(conn);
            return;
        }
        conn.getChannel().close();
        conn.getIntranetTunnel().deleteIntranetTunnelConnection(conn);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();

        if (conn.getChannel().isActive()) {
            conn.getChannel().pipeline().remove(IntranetTunnelRelayHandler.class);
            conn.getIntranetTunnel().returnIntranetTunnelConnection(conn);
            return;
        }
        conn.getChannel().close();
        conn.getIntranetTunnel().deleteIntranetTunnelConnection(conn);

    }
}
