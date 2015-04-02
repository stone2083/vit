package org.jellylab.vit.socks;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.jellylab.vit.IntranetTunnelConnection;
import org.jellylab.vit.tunnel.IntranetTunnel;
import org.jellylab.vit.tunnel.handler.IntranetTunnelIdleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 24, 2015
 */
public class SocksServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocksServerTest.class);

    public static void main(String[] args) throws Exception {
        IntranetTunnel intranetTunnel = IntranetTunnel.getIntranetTunnel();
        SocksServer server = new SocksServer();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        LOGGER.info("tunnel conns: {}; tunnel idles: {}", intranetTunnel.getConns().size(),
                                intranetTunnel.getIdles().size());

                        if (intranetTunnel.getConns().size() < 5) {
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
                        }

                        Thread.sleep(5000);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                }

            }
        }).start();

        server.setPort(3129);
        server.init();
        server.start();
    }
}
