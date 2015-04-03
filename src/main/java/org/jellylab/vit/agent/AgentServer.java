package org.jellylab.vit.agent;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.jellylab.vit.VitServer;
import org.jellylab.vit.agent.handler.AgentController;
import org.jellylab.vit.agent.handler.AgentInitEncoder;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentServer implements VitServer {

    private String tunnelServerHost;
    private int tunnelServerPort;

    private Agent agent;
    private volatile boolean running = false;

    private EventLoopGroup worker = new NioEventLoopGroup();

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start() throws Exception {
        running = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (running) {
                    for (AgentConnectionGroup group : agent.getGroups()) {
                        if (group.getConnsSize() < group.getMaxConns()) {
                            Bootstrap b = new Bootstrap();
                            b.group(worker);
                            b.channel(NioSocketChannel.class);
                            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
                            b.option(ChannelOption.SO_KEEPALIVE, true);
                            b.handler(new ChannelInitializer<SocketChannel>() {

                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline cp = ch.pipeline();
                                    cp.addLast(new AgentInitEncoder());
                                    cp.addLast(new AgentController(group));
                                }
                            });
                            b.connect(tunnelServerHost, tunnelServerPort);
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }

            }
        }).start();
    }

    @Override
    public void shutdown() throws Exception {
        running = false;
    }

    public void setTunnelServerHost(String tunnelServerHost) {
        this.tunnelServerHost = tunnelServerHost;
    }

    public void setTunnelServerPort(int tunnelServerPort) {
        this.tunnelServerPort = tunnelServerPort;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
        this.agent.setAgentServer(this);
    }

    public EventLoopGroup getWorker() {
        return worker;
    }

}
