package org.jellylab.vit.agent;

import java.util.List;

/**
 * @author jinli Apr 3, 2015
 */
public class AgentConfiguration {

    private int nworker;
    private List<TunnelServerAddress> tunnelServerAddresses;
    private List<AgentAddress> agentAddresses;

    public int getNworker() {
        return nworker;
    }

    public void setNworker(int nworker) {
        this.nworker = nworker;
    }

    public List<TunnelServerAddress> getTunnelServerAddresses() {
        return tunnelServerAddresses;
    }

    public void setTunnelServerAddresses(List<TunnelServerAddress> tunnelServerAddresses) {
        this.tunnelServerAddresses = tunnelServerAddresses;
    }

    public List<AgentAddress> getAgentAddresses() {
        return agentAddresses;
    }

    public void setAgentAddresses(List<AgentAddress> agentAddresses) {
        this.agentAddresses = agentAddresses;
    }

    public static class TunnelServerAddress {

        private String host;
        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class AgentAddress {
        private String ehost;
        private int eport;
        private String sign;
        private int maxConns;

        private List<ServerAddress> serverAddresses;

        public String getEhost() {
            return ehost;
        }

        public void setEhost(String ehost) {
            this.ehost = ehost;
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

        public List<ServerAddress> getServerAddresses() {
            return serverAddresses;
        }

        public void setServerAddresses(List<ServerAddress> serverAddresses) {
            this.serverAddresses = serverAddresses;
        }

    }

    public static class ServerAddress {

        private String host;
        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

    }

}
