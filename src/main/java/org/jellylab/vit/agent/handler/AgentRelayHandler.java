package org.jellylab.vit.agent.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import org.jellylab.vit.agent.AgentConnection;

/**
 * @author jinli Apr 3, 2015
 */
public class AgentRelayHandler extends ChannelInboundHandlerAdapter {

    private AgentConnection conn;

    public AgentRelayHandler(AgentConnection conn) {
        this.conn = conn;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (conn.getServerChannel() == null || !conn.getServerChannel().isActive()) {
            InetSocketAddress serverAddress = conn.getGroup().getNextServerAddresses();
            Bootstrap b = new Bootstrap();
            // b.group(ctx.channel().eventLoop());
            b.group(new NioEventLoopGroup());
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new AgentServerRelayHandler(conn));
            Channel ch = b.connect(serverAddress).sync().channel();
            conn.setServerChannel(ch);
        }

        conn.getServerChannel().writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        if (conn.getServerChannel() != null) {
            conn.getServerChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        if (conn.getServerChannel() != null) {
            conn.getServerChannel().close();
        }
    }

}
