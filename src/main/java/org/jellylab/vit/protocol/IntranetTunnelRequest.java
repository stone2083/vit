package org.jellylab.vit.protocol;

/**
 * @author jinli Apr 2, 2015
 */
public abstract class IntranetTunnelRequest extends IntranetTunnelMessage {

    private IntranetTunnelRequestType requestType = IntranetTunnelRequestType.UNKNOWN;

    public IntranetTunnelRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(IntranetTunnelRequestType requestType) {
        this.requestType = requestType;
    }

    public static enum IntranetTunnelRequestType {
        INIT,           // init
        UNKNOWN;        // unknown
    }
}
