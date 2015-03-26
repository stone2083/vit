package org.jellylab.vit.tunnel.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.SocksConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 25, 2015
 */
public class IntranetTunnelRelayHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntranetTunnelRelayHandler.class);

    private SocksConnection conn;

    public IntranetTunnelRelayHandler(SocksConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("tunnel read. remote: {}", ctx.channel().remoteAddress());
        conn.getChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("tunnel closed. remote: {}", ctx.channel().remoteAddress());
        if (conn.getChannel().isActive()) {
            conn.getChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.debug("tunnel exception. remote: {}", ctx.channel().remoteAddress());
        ctx.close();
        conn.getChannel().close();
    }

}
