package org.jellylab.vit.socks;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.jellylab.vit.IntranetTunnelConnection;
import org.jellylab.vit.tunnel.IntranetTunnel;
import org.jellylab.vit.tunnel.handler.IntranetTunnelIdleHandler;

/**
 * @author jinli Mar 24, 2015
 */
public class SocksServerTest {

    public static void main(String[] args) throws Exception {
        IntranetTunnel intranetTunnel = IntranetTunnel.getIntranetTunnel();
        SocksServer server = new SocksServer();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    System.out.println(intranetTunnel.getConns().size());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }

            }
        }).start();

        IntranetTunnelConnection conn = new IntranetTunnelConnection();
        Bootstrap b = new Bootstrap();
        b.group(server.getWorker());
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new IntranetTunnelIdleHandler(conn));
        Channel ch = b.connect("42.159.159.175", 80).sync().channel();

        conn.setEip("42.159.159.175");
        conn.setEport(80);
        conn.setChannel(ch);
        intranetTunnel.addIntranetTunnelConnection(conn);

        server.setPort(3129);
        server.init();
        server.start();
    }
}
