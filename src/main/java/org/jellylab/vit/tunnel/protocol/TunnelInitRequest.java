package org.jellylab.vit.tunnel.protocol;

import static org.jellylab.vit.utils.ProtocolUtil.ENCODING;
import io.netty.buffer.ByteBuf;

/**
 * @author jinli Apr 2, 2015
 */
public class TunnelInitRequest extends TunnelRequest {

    private TunnelVersion version;
    private String ehost;
    private int eport;
    private String sign;

    public TunnelVersion getVersion() {
        return version;
    }

    public void setVersion(TunnelVersion version) {
        this.version = version;
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
        byteBuf.writeByte(ehost.length()).writeBytes(ehost.getBytes(ENCODING));
        byteBuf.writeShort(eport);
        byteBuf.writeByte(sign.length());
        byteBuf.writeBytes(sign.getBytes());
    }

}
