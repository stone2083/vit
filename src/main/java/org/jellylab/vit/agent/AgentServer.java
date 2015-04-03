package org.jellylab.vit.agent;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.jellylab.vit.VitServer;
import org.jellylab.vit.agent.handler.AgentController;
import org.jellylab.vit.agent.handler.AgentInitEncoder;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentServer implements VitServer {

    private List<InetSocketAddress> tunnelAddresses;
    private AtomicLong loop = new AtomicLong(0);

    private Agent agent;
    private volatile boolean running = false;

    private EventLoopGroup worker;
    private int nworker;

    @Override
    public void init() throws Exception {
        worker = new NioEventLoopGroup(nworker);
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
                            for (int i = 0; i < group.getMaxConns() - group.getConnsSize(); i++) {
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

                                b.connect(getNextTunnelAddress());
                            }
                        }
                    }

                    try {
                        Thread.sleep(100);
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

    private InetSocketAddress getNextTunnelAddress() {
        return tunnelAddresses.get((int) (Math.abs(loop.getAndIncrement()) % tunnelAddresses.size()));
    }

    public List<InetSocketAddress> getTunnelAddresses() {
        if (tunnelAddresses == null) {
            tunnelAddresses = new ArrayList<InetSocketAddress>();
        }
        return tunnelAddresses;
    }

    public void setTunnelAddresses(List<InetSocketAddress> tunnelAddresses) {
        this.tunnelAddresses = tunnelAddresses;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
        this.agent.setAgentServer(this);
    }

    public EventLoopGroup getWorker() {
        return worker;
    }

    public void setNworker(int nworker) {
        this.nworker = nworker;
    }

}
