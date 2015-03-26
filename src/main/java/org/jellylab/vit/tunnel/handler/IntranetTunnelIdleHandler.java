package org.jellylab.vit.tunnel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.IntranetTunnelConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 25, 2015
 */
public class IntranetTunnelIdleHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntranetTunnelIdleHandler.class);

    private IntranetTunnelConnection conn;

    public IntranetTunnelIdleHandler(IntranetTunnelConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.warn("tunnel idle. remote address: {}", ctx.channel().remoteAddress());
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
