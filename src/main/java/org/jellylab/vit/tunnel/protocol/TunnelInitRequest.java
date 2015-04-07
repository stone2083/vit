package org.jellylab.vit.tunnel.protocol;

import io.netty.buffer.ByteBuf;

import org.jellylab.vit.utils.ProtocolUtil;

/**
 * @author jinli Apr 2, 2015
 */
public class TunnelInitRequest extends TunnelRequest {

    private TunnelVersion version;
    private TunnelAddressType addressType;
    private String ehost;
    private int eport;
    private String sign;

    public TunnelVersion getVersion() {
        return version;
    }

    public void setVersion(TunnelVersion version) {
        this.version = version;
    }

    public TunnelAddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(TunnelAddressType addressType) {
        this.addressType = addressType;
    }

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

    @Override
    public void encode(ByteBuf byteBuf) {
        byteBuf.writeByte(version.byteValue());
        byteBuf.writeByte(addressType.byteValue());
        byteBuf.writeInt(ProtocolUtil.ipv4ToInt(ehost));
        byteBuf.writeShort(eport);
        byteBuf.writeByte(sign.length());
        byteBuf.writeBytes(sign.getBytes());
    }

    @Override
    public String toString() {
        return "IntranetTunnelInitRequest [version=" + version + ", addressType=" + addressType + ", ehost=" + ehost
                + ", eport=" + eport + ", sign=" + sign + "]";
    }

}
