package org.jellylab.vit.agent.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import org.jellylab.vit.agent.AgentConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Apr 3, 2015
 */
public class AgentRelayHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentRelayHandler.class);

    private AgentConnection conn;

    public AgentRelayHandler(AgentConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("agent read. remote address: {}", ctx.channel().remoteAddress());

        if (conn.getServerChannel() == null || !conn.getServerChannel().isActive()) {
            InetSocketAddress serverAddress = conn.getGroup().getNextServerAddresses();
            Bootstrap b = new Bootstrap();
            b.group(conn.getGroup().getAgent().getAgentServer().getWorker());
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new AgentServerRelayHandler(conn));
            Channel ch = b.connect(serverAddress).sync().channel();
            conn.setServerChannel(ch);
            LOGGER.debug("server connected. remote address: {}", ch.remoteAddress());
        }

        conn.getServerChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("agent closed. remote address: {}", ctx.channel().remoteAddress());
        ctx.close();
        conn.getGroup().deleteAgentConnection(conn);
        if (conn.getServerChannel() != null) {
            conn.getServerChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.debug("agent exception. remote address: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }

}
