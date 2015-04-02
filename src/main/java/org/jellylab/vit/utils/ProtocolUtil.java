package org.jellylab.vit.utils;

/**
 * @author jinli Apr 2, 2015
 */
public class ProtocolUtil {

    private static final int SECOND_ADDRESS_OCTET_SHIFT = 16;
    private static final int FIRST_ADDRESS_OCTET_SHIFT = 24;
    private static final int THIRD_ADDRESS_OCTET_SHIFT = 8;
    private static final int XOR_DEFAULT_VALUE = 0xff;

    public static String intToIpv4(int i) {
        return String.valueOf(i >> FIRST_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.' // x.0.0.0
                + (i >> SECOND_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.'           // x.x.0.0
                + (i >> THIRD_ADDRESS_OCTET_SHIFT & XOR_DEFAULT_VALUE) + '.'            // x.x.x.0
                + (i & XOR_DEFAULT_VALUE);                                              // x.y.x.x
    }

    public static int ipv4ToInt(String value) {
        if (!isValidIpv4(value)) {
            return -1;
        }
        String[] segs = value.split("\\.");
        int ret = 0;
        ret |= Integer.valueOf(segs[3]);
        ret |= Integer.valueOf(segs[2]) << THIRD_ADDRESS_OCTET_SHIFT;
        ret |= Integer.valueOf(segs[1]) << SECOND_ADDRESS_OCTET_SHIFT;
        ret |= Integer.valueOf(segs[0]) << FIRST_ADDRESS_OCTET_SHIFT;
        return ret;
    }

    public static boolean isValidIpv4(String value) {
        if (value == null) {
            return false;
        }
        int periods = 0;
        int i;
        int length = value.length();
        if (length > 15) {
            return false;
        }
        char c;
        StringBuilder word = new StringBuilder();
        for (i = 0; i < length; i++) {
            c = value.charAt(i);
            if (c == '.') {
                periods++;
                if (periods > 3) {
                    return false;
                }
                if (word.length() == 0) {
                    return false;
                }
                if (Integer.parseInt(word.toString()) > 255) {
                    return false;
                }
                word.delete(0, word.length());
            } else if (!Character.isDigit(c)) {
                return false;
            } else {
                if (word.length() > 2) {
                    return false;
                }
                word.append(c);
            }
        }
        if (word.length() == 0 || Integer.parseInt(word.toString()) > 255) {
            return false;
        }
        return periods == 3;
    }

}
