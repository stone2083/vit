package org.jellylab.vit.protocol;

/**
 * @author jinli Apr 2, 2015
 */
public enum TunnelAddressType {

    IPv4((byte) 0x01),          // ipv4
    IPv6((byte) 0x02),          // ipv6
    DOMAIN((byte) 0x03),        // domain
    UNKNOWN((byte) 0xff);       // unknown

    private byte b;

    private TunnelAddressType(byte b) {
        this.b = b;
    }

    public static TunnelAddressType valueOf(byte b) {
        for (TunnelAddressType type : values()) {
            if (type.b == b) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public byte byteValue() {
        return b;
    }
}
