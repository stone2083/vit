package org.jellylab.vit.agent;

import org.jellylab.vit.tunnel.IntranetTunnelServer;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentServerTest {

    public static void main(String[] args) throws Exception {
        IntranetTunnelServer server = new IntranetTunnelServer();
        server.setPort(9999);
        server.start();

        AgentConnectionGroup group = new AgentConnectionGroup();
        group.setEip("1.1.1.1");
        group.setEport(1);
        group.setSign("sign");
        group.setMaxConns(2);

        Agent agent = new Agent();
        agent.addAgentConnectionGroup(group);

        AgentServer agentServer = new AgentServer();
        agentServer.setTunnelServerHost("127.0.0.1");
        agentServer.setTunnelServerPort(9999);
        agentServer.setAgent(agent);
        agentServer.init();
        agentServer.start();
    }

}
