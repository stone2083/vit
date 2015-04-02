package org.jellylab.vit.protocol;

import io.netty.buffer.ByteBuf;

import org.jellylab.vit.utils.ProtocolUtil;

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

    @Override
    public void encode(ByteBuf byteBuf) {
        byteBuf.writeByte(version.byteValue());
        byteBuf.writeByte(addressType.byteValue());
        byteBuf.writeInt(ProtocolUtil.ipv4ToInt(eip));
        byteBuf.writeShort(eport);
        byteBuf.writeByte(sign.length());
        byteBuf.writeBytes(sign.getBytes());
    }

    @Override
    public String toString() {
        return "IntranetTunnelInitRequest [version=" + version + ", addressType=" + addressType + ", eip=" + eip
                + ", eport=" + eport + ", sign=" + sign + "]";
    }

}
