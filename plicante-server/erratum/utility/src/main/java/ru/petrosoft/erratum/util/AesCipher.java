package ru.petrosoft.erratum.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 */
public class AesCipher {

    public static String cipher(String content, String key) {
        try {
            byte[] aesKey = DatatypeConverter.parseBase64Binary(key);
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encodedContent = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printBase64Binary(encodedContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
