package org.jellylab.vit.tunnel;

import java.io.File;

import org.jellylab.vit.tunnel.socks.SocksServer;
import org.jellylab.vit.utils.IoUtil;

import com.alibaba.fastjson.JSON;

/**
 * @author jinli Apr 3, 2015
 */
public class TunnelServerMain {

    public static void main(String[] args) {
        String conf = "tunnel.conf";
        if (System.getProperty("tunnel.conf") != null) {
            conf = System.getProperty("tunnel.conf");
        }
        File fconf = new File(conf);
        if (!fconf.isFile()) {
            System.out.println("tunnel.conf not found. [conf=" + conf + "]");
            return;
        }

        TunnelConfiguration configuration;
        try {
            configuration = JSON.parseObject(IoUtil.read(fconf, "utf-8"), TunnelConfiguration.class);
        } catch (Exception e) {
            System.out.println("tunnel.conf invalid formats.");
            return;
        }

        TunnelServer tunnelServer = new TunnelServer();
        tunnelServer.setPort(configuration.getTunnel().getPort());
        tunnelServer.setNworker(configuration.getTunnel().getNwoker());
        try {
            tunnelServer.init();
            tunnelServer.start();
        } catch (Exception e) {
            System.out.println("tunnelServer init and start fail.");
            return;
        }

        SocksServer socksServer = new SocksServer();
        socksServer.setPort(configuration.getSocks().getPort());
        socksServer.setNworker(configuration.getSocks().getNwoker());
        try {
            socksServer.init();
            socksServer.start();
        } catch (Exception e) {
            System.out.println("socksServer init and start fail.");
            try {
                tunnelServer.shutdown();
            } catch (Exception e1) {
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    socksServer.shutdown();
                    tunnelServer.shutdown();
                } catch (Exception e) {
                }
            }
        }));

    }

}
