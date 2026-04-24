package com.carrental.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for hashing and verifying passwords using SHA-256 + salt.
 * In production, prefer BCrypt (e.g., via Spring Security).
 */
public final class PasswordUtil {

    private PasswordUtil() {}

    /** Generates a random 16-byte salt encoded as Base64. */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /** Returns SHA-256 hash of (salt + password) encoded as Base64. */
    public static String hash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashed = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Hashes password and stores "salt$hash" as a single string.
     * This is what gets saved to User_Credential.password_hash.
     */
    public static String createPasswordHash(String plainPassword) {
        String salt = generateSalt();
        String hash = hash(plainPassword, salt);
        return salt + "$" + hash;
    }

    /**
     * Verifies a plain password against a stored "salt$hash" value.
     */
    public static boolean verify(String plainPassword, String storedHash) {
        String[] parts = storedHash.split("\\$", 2);
        if (parts.length != 2) return false;
        String computed = hash(plainPassword, parts[0]);
        return computed.equals(parts[1]);
    }
}
