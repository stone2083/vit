package org.jellylab.vit;

import org.jellylab.vit.agent.AgentServerMain;
import org.jellylab.vit.tunnel.TunnelServerMain;

/**
 * @author jinli Apr 3, 2015
 */
public class VitServerTest {

    public static void main(String[] args) {
        System.setProperty("tunnel.cnf", "src/main/resources/cnf/tunnel.cnf");
        System.setProperty("agent.cnf", "src/main/resources/cnf/agent.cnf");
        TunnelServerMain.main(args);
        AgentServerMain.main(args);
    }

}
