package org.jellylab.vit.agent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author jinli Apr 2, 2015
 */
public class Agent {

    private BlockingQueue<AgentConnectionGroup> groups = new LinkedBlockingDeque<AgentConnectionGroup>();

    public BlockingQueue<AgentConnectionGroup> getGroups() {
        return groups;
    }

    public void addAgentConnectionGroup(AgentConnectionGroup group) {
        if (group == null || group.getEip() == null) {
            return;
        }
        for (AgentConnectionGroup g : groups) {
            if (g.getEip().equals(group.getEip()) && g.getEport() == group.getEport()) {
                return;
            }
        }
        groups.add(group);
    }

    public void deleteAgentConnectionGroup(AgentConnectionGroup group) {
        groups.remove(group);
    }

}
