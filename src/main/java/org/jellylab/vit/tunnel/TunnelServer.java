package org.jellylab.vit.tunnel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.jellylab.vit.VitServer;
import org.jellylab.vit.tunnel.handler.TunnelController;
import org.jellylab.vit.tunnel.handler.TunnelInitDecoder;
import org.jellylab.vit.tunnel.handler.TunnelResponseEncoder;

/**
 * @author jinli Mar 26, 2015
 */
public class TunnelServer implements VitServer {

    private int port;

    private EventLoopGroup boss = new NioEventLoopGroup(1);
    private EventLoopGroup worker = new NioEventLoopGroup();
    private Tunnel tunnel = Tunnel.getTunnel();

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker);
        b.channel(NioServerSocketChannel.class);
        b.handler(new LoggingHandler(LogLevel.INFO));
        b.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast(new TunnelResponseEncoder());
                cp.addLast(new TunnelInitDecoder());
                cp.addLast(new TunnelController(tunnel));
            }
        });
        b.bind(port).sync();

    }

    @Override
    public void shutdown() throws Exception {
        boss.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public EventLoopGroup getBoss() {
        return boss;
    }

    public EventLoopGroup getWorker() {
        return worker;
    }

}
