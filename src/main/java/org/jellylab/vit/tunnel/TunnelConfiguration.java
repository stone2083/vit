package org.jellylab.vit.tunnel;

/**
 * @author jinli Apr 3, 2015
 */
public class TunnelConfiguration {

    private Tunnel tunnel;
    private Socks socks;

    public Tunnel getTunnel() {
        return tunnel;
    }

    public void setTunnel(Tunnel tunnel) {
        this.tunnel = tunnel;
    }

    public Socks getSocks() {
        return socks;
    }

    public void setSocks(Socks socks) {
        this.socks = socks;
    }

    public static class Tunnel {

        private int port;
        private int nwoker;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getNwoker() {
            return nwoker;
        }

        public void setNwoker(int nwoker) {
            this.nwoker = nwoker;
        }

    }

    public static class Socks {

        private int port;
        private int nwoker;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getNwoker() {
            return nwoker;
        }

        public void setNwoker(int nwoker) {
            this.nwoker = nwoker;
        }

    }

}
