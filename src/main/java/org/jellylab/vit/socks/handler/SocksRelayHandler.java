package org.jellylab.vit.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.IntranetTunnelConnection;
import org.jellylab.vit.tunnel.handler.IntranetTunnelRelayHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 25, 2015
 */
public class SocksRelayHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocksRelayHandler.class);

    private IntranetTunnelConnection conn;

    public SocksRelayHandler(IntranetTunnelConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("socks read. remote: {}", ctx.channel().remoteAddress());
        conn.getChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("socks closed. remote: {}", ctx.channel().remoteAddress());
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
        LOGGER.debug("socks exception. remote: {}", ctx.channel().remoteAddress());
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
