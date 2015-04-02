package org.jellylab.vit.protocol;

/**
 * @author jinli Apr 2, 2015
 */
public class IntranetTunnelInitRequest extends IntranetTunnelRequest {

    private IntranetTunnelVersion version;
    private IntranetTunnelAddressType addressType;
    private String eip;
    private int eport;
    private String sign;

    public IntranetTunnelVersion getVersion() {
        return version;
    }

    public void setVersion(IntranetTunnelVersion version) {
        this.version = version;
    }

    public IntranetTunnelAddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(IntranetTunnelAddressType addressType) {
        this.addressType = addressType;
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

}
