package org.jellylab.vit;

import org.jellylab.vit.agent.AgentServerMain;
import org.jellylab.vit.tunnel.TunnelServerMain;

/**
 * @author jinli Apr 3, 2015
 */
public class VitServerTest {

    public static void main(String[] args) {
        System.setProperty("tunnel.conf", "src/main/resources/conf/tunnel.conf");
        System.setProperty("agent.conf", "src/main/resources/conf/agent.conf");
        TunnelServerMain.main(args);
        AgentServerMain.main(args);
    }

}
