package org.jellylab.vit.misc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

/**
 * @author jinli Apr 3, 2015
 */
public class Misc {

    public static void main(String[] args) throws Exception {
        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup());
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new SimpleChannelInboundHandler<ByteBuf>() {

            @Override
            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                System.out.println(msg.toString(Charset.defaultCharset()));
                ctx.close();
            }
        });
        Channel ch = b.connect("42.159.159.175", 80).sync().channel();

        ByteBuf byteBuf = ch.alloc().buffer();
        byteBuf.writeBytes("xxx\n".getBytes());
        ch.writeAndFlush(byteBuf).sync();
    }

}
