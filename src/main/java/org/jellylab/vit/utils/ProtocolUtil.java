package org.jellylab.vit.utils;

/**
 * @author jinli Apr 2, 2015
 */
public class ProtocolUtil {

    private static final int SECOND_ADDRESS_OCTET_SHIFT = 16;
    private static final int FIRST_ADDRESS_OCTET_SHIFT = 24;
    private static final int THIRD_ADDRESS_OCTET_SHIFT = 8;
    private static final int XOR_DEFAULT_VALUE = 0xff;

    public static String toIpv4(int i) {
        return String.valueOf(i >> FIRST_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.' // x.0.0.0
                + (i >> SECOND_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.'           // x.x.0.0
                + (i >> THIRD_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.'            // x.x.x.0
                + (i & XOR_DEFAULT_VALUE);                                              // x.y.x.x
    }

}
