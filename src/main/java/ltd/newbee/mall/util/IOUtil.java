package ltd.newbee.mall.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

    public static byte[] getBytes(InputStream in) throws IOException {
        final byte[] temp = new byte[1024];
        int len;
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
            while ((len = in.read(temp)) != 0) {
                arrayOutputStream.write(temp, 0, len);
            }
            return arrayOutputStream.toByteArray();
        }
    }
}
