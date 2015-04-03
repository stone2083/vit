package org.jellylab.vit.tunnel.socks;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;

import org.jellylab.vit.VitServer;
import org.jellylab.vit.tunnel.Tunnel;
import org.jellylab.vit.tunnel.socks.handler.SocksController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 24, 2015
 */
public class SocksServer implements VitServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocksServer.class);

    private int port;
    private int nworker;

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private Tunnel intranetTunnel = Tunnel.getTunnel();

    @Override
    public void init() throws Exception {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup(nworker);
    }

    @Override
    public void start() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast(new SocksMessageEncoder());
                cp.addLast(new SocksInitRequestDecoder());
                cp.addLast(new SocksController(intranetTunnel));
            }
        });
        b.bind(port).sync();
        LOGGER.info("socks server start.");
    }

    @Override
    public void shutdown() throws Exception {
        boss.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
        LOGGER.info("socks server shutdown.");
    }

    public EventLoopGroup getBoss() {
        return boss;
    }

    public EventLoopGroup getWorker() {
        return worker;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setNworker(int nworker) {
        this.nworker = nworker;
    }

}
