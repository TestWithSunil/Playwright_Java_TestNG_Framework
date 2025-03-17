package com.qa.demowebshop.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.FileInputStream;
import java.util.Properties;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static String secretKey;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));
            secretKey = properties.getProperty("encryption.key");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load encryption key!", e);
        }
    }

    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}

