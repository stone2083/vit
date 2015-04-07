package org.jellylab.vit.tunnel;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.jellylab.vit.tunnel.protocol.TunnelAddressType;
import org.jellylab.vit.tunnel.protocol.TunnelInitRequest;
import org.jellylab.vit.tunnel.protocol.TunnelVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Apr 2, 2015
 */
public class IntranetTunnelServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntranetTunnelServerTest.class);

    public static void main(String[] args) throws Exception {
        TunnelServer server = new TunnelServer();
        server.setPort(9999);
        server.init();
        server.start();

        Bootstrap b = new Bootstrap();
        b.group(server.getWorker());
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new Handler());

        Channel ch = b.connect("127.0.0.1", 9999).sync().channel();

        TunnelInitRequest req = new TunnelInitRequest();
        req.setVersion(TunnelVersion.IntranetTunnelV1);
        req.setAddressType(TunnelAddressType.IPv4);
        req.setEhost("250.250.250.250");
        req.setEport(80);
        req.setSign("sign");
        ByteBuf byteBuf = ch.alloc().buffer();
        req.encode(byteBuf);
        ch.writeAndFlush(byteBuf);
    }

    private static class Handler extends ChannelInboundHandlerAdapter {

        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LOGGER.info(String.valueOf(((ByteBuf) msg).readByte()));
        }

    }

}
