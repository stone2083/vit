package org.jellylab.vit.protocol;

/**
 * @author jinli Apr 2, 2015
 */
public enum IntranetTunnelVersion {

    IntranetTunnelV1((byte) 0x01),      // version 1
    UNKNOWN((byte) 0xff);               // version unknown

    private byte b;

    private IntranetTunnelVersion(byte b) {
        this.b = b;
    }

    public static IntranetTunnelVersion valueOf(byte b) {
        for (IntranetTunnelVersion code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return b;
    }

}
