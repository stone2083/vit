package org.jellylab.vit.misc;

import java.util.ArrayList;

import org.jellylab.vit.agent.AgentConfiguration;
import org.jellylab.vit.agent.AgentConfiguration.AgentAddress;
import org.jellylab.vit.agent.AgentConfiguration.ServerAddress;
import org.jellylab.vit.agent.AgentConfiguration.TunnelServerAddress;
import org.jellylab.vit.tunnel.TunnelConfiguration;
import org.jellylab.vit.tunnel.TunnelConfiguration.Socks;
import org.jellylab.vit.tunnel.TunnelConfiguration.Tunnel;

import com.alibaba.fastjson.JSON;

/**
 * @author jinli Apr 3, 2015
 */
public class Misc {

    public static void main(String[] args) throws Exception {
        TunnelConfiguration cfg = new TunnelConfiguration();
        cfg.setTunnel(new Tunnel());
        cfg.getTunnel().setNwoker(0);
        cfg.getTunnel().setPort(9999);
        cfg.setSocks(new Socks());
        cfg.getSocks().setNwoker(0);
        cfg.getSocks().setPort(3129);

        System.out.println(JSON.toJSON(cfg));

        AgentConfiguration ac = new AgentConfiguration();
        ac.setNworker(0);
        ac.setTunnelServerAddresses(new ArrayList<AgentConfiguration.TunnelServerAddress>());
        ac.getTunnelServerAddresses().add(new TunnelServerAddress());
        ac.getTunnelServerAddresses().get(0).setHost("127.0.0.1");
        ac.getTunnelServerAddresses().get(0).setPort(9999);

        ac.setAgentAddresses(new ArrayList<AgentAddress>());
        ac.getAgentAddresses().add(new AgentAddress());
        ac.getAgentAddresses().get(0).setEhost("1.1.1.1");
        ac.getAgentAddresses().get(0).setEport(1);
        ac.getAgentAddresses().get(0).setMaxConns(10);
        ac.getAgentAddresses().get(0).setSign("sign");
        ac.getAgentAddresses().get(0).setServerAddresses(new ArrayList<ServerAddress>());
        ac.getAgentAddresses().get(0).getServerAddresses().add(new ServerAddress());
        ac.getAgentAddresses().get(0).getServerAddresses().get(0).setHost("ip.cn");
        ac.getAgentAddresses().get(0).getServerAddresses().get(0).setPort(80);

        System.out.println(JSON.toJSON(ac));
    }
}
