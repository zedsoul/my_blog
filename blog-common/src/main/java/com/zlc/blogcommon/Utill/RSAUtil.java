package com.zlc.blogcommon.Utill;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * @author Assinss
 * @date 2023/11/20
 * RSA加密解密工具类
 */
public class RSAUtil {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String SALT = "zlc7808696"; // 自定义盐值
    //生成密钥对
    public static KeyPair keyPair;

    static {
        try {
            keyPair = generateRSAKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //公钥
    public static PublicKey publicKey = keyPair.getPublic();
    //私钥
    public static PrivateKey privateKey = keyPair.getPrivate();


    public static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(1024); // 设置RSA密钥长度
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String plainText, PublicKey publicKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] saltedPassword = (plainText + SALT).getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(saltedPassword);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(hash);

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encryptedText, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
