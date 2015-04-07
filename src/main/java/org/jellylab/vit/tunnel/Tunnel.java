package org.jellylab.vit.tunnel;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author jinli Mar 25, 2015
 */
public class Tunnel {

    private BlockingQueue<TunnelConnection> conns = new LinkedBlockingDeque<TunnelConnection>();
    private BlockingQueue<TunnelConnection> idles = new LinkedBlockingDeque<TunnelConnection>();

    private static final Tunnel singleton = new Tunnel();

    public static final Tunnel getTunnel() {
        return singleton;
    }

    public boolean auth(String ehost, int eport, String sign) {
        return true;
    }

    public void addIntranetTunnelConnection(TunnelConnection conn) {
        conn.setTunnel(this);
        conns.add(conn);
        idles.add(conn);
    }

    public void deleteIntranetTunnelConnection(TunnelConnection conn) {
        conns.remove(conn);
        idles.remove(conn);
    }

    public TunnelConnection borrowIntranetTunnelConnection(String ehost, int eport) {
        if (idles.isEmpty()) {
            return null;
        }
        Iterator<TunnelConnection> it = idles.iterator();
        while (it.hasNext()) {
            TunnelConnection conn = it.next();
            if (ehost.equals(conn.getEhost()) && eport == conn.getEport()) {
                it.remove();
                return conn;
            }
        }
        return null;
    }

    public void returnIntranetTunnelConnection(TunnelConnection conn) {
        if (conns.contains(conn)) {
            idles.add(conn);
        }
    }

    public BlockingQueue<TunnelConnection> getConns() {
        return conns;
    }

    public BlockingQueue<TunnelConnection> getIdles() {
        return idles;
    }

}
