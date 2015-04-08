package org.jellylab.vit.tunnel.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.tunnel.TunnelConnection;
import org.jellylab.vit.tunnel.handler.TunnelRelayHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 25, 2015
 */
public class SocksRelayHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocksRelayHandler.class);

    private TunnelConnection conn;

    public SocksRelayHandler(TunnelConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("socks read. remote address: {}", ctx.channel().remoteAddress());
        conn.getChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("socks closed. remote address: {}", ctx.channel().remoteAddress());
        conn.getChannel().pipeline().remove(TunnelRelayHandler.class);
        conn.getTunnel().returnIntranetTunnelConnection(conn);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.debug("socks exception. remote address: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
