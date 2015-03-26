package org.jellylab.vit.tunnel;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.jellylab.vit.IntranetTunnelConnection;

/**
 * @author jinli Mar 25, 2015
 */
public class IntranetTunnel {

    private BlockingQueue<IntranetTunnelConnection> conns = new LinkedBlockingDeque<IntranetTunnelConnection>();
    private BlockingQueue<IntranetTunnelConnection> idles = new LinkedBlockingDeque<IntranetTunnelConnection>();

    private static final IntranetTunnel singleton = new IntranetTunnel();

    public static final IntranetTunnel getIntranetTunnel() {
        return singleton;
    }

    public void addIntranetTunnelConnection(IntranetTunnelConnection conn) {
        conn.setIntranetTunnel(this);
        conns.add(conn);
        idles.add(conn);
    }

    public void deleteIntranetTunnelConnection(IntranetTunnelConnection conn) {
        conns.remove(conn);
        idles.remove(conn);
    }

    public IntranetTunnelConnection borrowIntranetTunnelConnection(String eip, int eport) {
        if (idles.isEmpty()) {
            return null;
        }
        Iterator<IntranetTunnelConnection> it = idles.iterator();
        while (it.hasNext()) {
            IntranetTunnelConnection conn = it.next();
            if (eip.equals(conn.getEip()) && eport == conn.getEport()) {
                it.remove();
                return conn;
            }
        }
        return null;
    }

    public void returnIntranetTunnelConnection(IntranetTunnelConnection conn) {
        if (conns.contains(conn)) {
            idles.add(conn);
        }
    }

    public BlockingQueue<IntranetTunnelConnection> getConns() {
        return conns;
    }

    public BlockingQueue<IntranetTunnelConnection> getIdles() {
        return idles;
    }

}
