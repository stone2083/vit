package org.jellylab.vit.agent;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentConnectionGroup {

    private Agent agent;

    private String eip;
    private int eport;
    private String sign;
    private int maxConns;

    private List<InetSocketAddress> serverAddresses;
    private AtomicLong loop = new AtomicLong(0);

    private BlockingQueue<AgentConnection> conns = new LinkedBlockingDeque<AgentConnection>();

    public boolean addAgentConnection(AgentConnection conn) {
        if (conns.size() > maxConns) {
            return false;
        }
        conn.setGroup(this);
        conns.add(conn);
        return true;
    }

    public void deleteAgentConnection(AgentConnection conn) {
        conns.remove(conn);
    }

    public int getConnsSize() {
        return conns.size();
    }

    public InetSocketAddress getNextServerAddresses() {
        return serverAddresses.get((int) (Math.abs(loop.getAndIncrement()) % serverAddresses.size()));
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getEip() {
        return eip;
    }

    public void setEip(String eip) {
        this.eip = eip;
    }

    public int getEport() {
        return eport;
    }

    public void setEport(int eport) {
        this.eport = eport;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getMaxConns() {
        return maxConns;
    }

    public void setMaxConns(int maxConns) {
        this.maxConns = maxConns;
    }

    public List<InetSocketAddress> getServerAddresses() {
        if (serverAddresses == null) {
            serverAddresses = new ArrayList<InetSocketAddress>(0);
        }
        return serverAddresses;
    }

    public void setServerAddresses(List<InetSocketAddress> serverAddresses) {
        this.serverAddresses = serverAddresses;
    }

}
