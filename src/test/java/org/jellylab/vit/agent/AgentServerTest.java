package org.jellylab.vit.agent;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.jellylab.vit.tunnel.TunnelServer;
import org.jellylab.vit.tunnel.socks.SocksServer;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentServerTest {

    public static void main(String[] args) throws Exception {
        TunnelServer server = new TunnelServer();
        server.init();
        server.setPort(9999);
        server.start();

        SocksServer socksServer = new SocksServer();
        socksServer.setPort(3129);
        socksServer.init();
        socksServer.start();

        AgentConnectionGroup group = new AgentConnectionGroup();
        group.setEhost("1.1.1.1");
        group.setEport(1);
        group.setSign("sign");
        group.setMaxConns(2);
        List<InetSocketAddress> serverAddresses = new ArrayList<InetSocketAddress>();
        serverAddresses.add(new InetSocketAddress("ip.cn", 80));
        group.setServerAddresses(serverAddresses);

        Agent agent = new Agent();
        agent.addAgentConnectionGroup(group);

        AgentServer agentServer = new AgentServer();
        List<InetSocketAddress> tunnelAddresses = new ArrayList<InetSocketAddress>();
        tunnelAddresses.add(new InetSocketAddress("127.0.0.1", 9999));
        agentServer.setAgent(agent);
        agentServer.setTunnelAddresses(tunnelAddresses);
        agentServer.init();
        agentServer.start();
    }

}
