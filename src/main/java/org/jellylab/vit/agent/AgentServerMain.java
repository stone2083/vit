package org.jellylab.vit.agent;

import java.io.File;
import java.net.InetSocketAddress;

import org.jellylab.vit.agent.handler.AgentConfiguration;
import org.jellylab.vit.agent.handler.AgentConfiguration.AgentAddress;
import org.jellylab.vit.agent.handler.AgentConfiguration.ServerAddress;
import org.jellylab.vit.agent.handler.AgentConfiguration.TunnelServerAddress;
import org.jellylab.vit.utils.IoUtil;

import com.alibaba.fastjson.JSON;

/**
 * @author jinli Apr 3, 2015
 */
public class AgentServerMain {

    public static void main(String[] args) {
        String cnf = "agent.cnf";
        if (System.getProperty("agent.cnf") != null) {
            cnf = System.getProperty("agent.cnf");
        }
        File fcnf = new File(cnf);
        if (!fcnf.isFile()) {
            System.out.println("agent.cnf not found. [cnf=" + cnf + "]");
            return;
        }

        AgentConfiguration configuration;
        try {
            configuration = JSON.parseObject(IoUtil.read(fcnf, "utf-8"), AgentConfiguration.class);
        } catch (Exception e) {
            System.out.println("agent.cnf invalid formats.");
            return;
        }

        Agent agent = new Agent();
        for (AgentAddress aa : configuration.getAgentAddresses()) {
            AgentConnectionGroup group = new AgentConnectionGroup();
            group.setEip(aa.getEip());
            group.setEport(aa.getEport());
            group.setSign(aa.getSign());
            group.setMaxConns(aa.getMaxConns());

            for (ServerAddress sa : aa.getServerAddresses()) {
                group.getServerAddresses().add(new InetSocketAddress(sa.getHost(), sa.getPort()));
            }

            agent.addAgentConnectionGroup(group);
        }

        AgentServer agentServer = new AgentServer();
        agentServer.setAgent(agent);
        agentServer.setNworker(configuration.getNworker());
        for (TunnelServerAddress tsa : configuration.getTunnelServerAddresses()) {
            agentServer.getTunnelAddresses().add(new InetSocketAddress(tsa.getHost(), tsa.getPort()));
        }

        try {
            agentServer.init();
            agentServer.start();
        } catch (Exception e) {
            System.out.println("init and start fail.");
        }

    }

}
