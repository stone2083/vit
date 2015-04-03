package org.jellylab.vit.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jinli Apr 3, 2015
 */
public class IoUtil {

    public static String read(File file, String encoding) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return new String(out.toByteArray(), encoding);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
