package org.jellylab.vit.agent;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentConnectionGroup {

    private String eip;
    private int eport;
    private String sign;
    private int maxConns;

    private List<InetAddress> addresses;
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

    public InetAddress getNextInetAddress() {
        return addresses.get((int) (Math.abs(loop.getAndIncrement()) % addresses.size()));
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

}
