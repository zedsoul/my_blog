package com.zlc.blogcommon.Utill;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {
    private static final int ITERATIONS = 10000; // 迭代次数
    private static final int SALT_LENGTH = 16; // 盐值长度

    public static String hashPassword(String password) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            secureRandom.nextBytes(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);

            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hashedBytes = digest.digest(hashedBytes);
            }

            byte[] finalBytes = new byte[salt.length + hashedBytes.length];
            System.arraycopy(salt, 0, finalBytes, 0, salt.length);
            System.arraycopy(hashedBytes, 0, finalBytes, salt.length, hashedBytes.length);

            return Base64.getEncoder().encodeToString(finalBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(hashedPassword);
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(decodedBytes, 0, salt, 0, SALT_LENGTH);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);

            byte[] hashedInput = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hashedInput = digest.digest(hashedInput);
            }

            byte[] storedPassword = new byte[decodedBytes.length - SALT_LENGTH];
            System.arraycopy(decodedBytes, SALT_LENGTH, storedPassword, 0, storedPassword.length);

            // 比较两个哈希后的密码是否相同
            if (storedPassword.length != hashedInput.length) {
                return false;
            }

            for (int i = 0; i < storedPassword.length; i++) {
                if (storedPassword[i] != hashedInput[i]) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
