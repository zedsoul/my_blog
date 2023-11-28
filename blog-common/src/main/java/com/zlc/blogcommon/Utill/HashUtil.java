package com.zlc.blogcommon.Utill;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {
    private static final int ITERATIONS = 100; // 迭代次数
    private static final int SALT_LENGTH = 16; // 盐值长度

    public static String hashPassword(String password) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            secureRandom.nextBytes(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);

            byte[] hashedBytes = digest.digest(password.getBytes());

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

            byte[] hashedInput = digest.digest(password.getBytes());

            for (int i = 0; i < ITERATIONS; i++) {
                digest.reset();
                hashedInput = digest.digest(hashedInput);
            }

            byte[] storedPassword = new byte[decodedBytes.length - SALT_LENGTH];
            System.arraycopy(decodedBytes, SALT_LENGTH, storedPassword, 0, storedPassword.length);

            return MessageDigest.isEqual(storedPassword, hashedInput);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        String originalPassword = "111";

        // 加密密码
        String hashedPassword = hashPassword(originalPassword);
        System.out.println("Hashed Password: " + hashedPassword);

        // 验证密码
        boolean passwordMatches = verifyPassword(originalPassword, hashedPassword);
        System.out.println("Password Matches: " + passwordMatches);
    }
}
