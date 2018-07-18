package com.snm.security.util;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    private enum Digest {

        md5("md5"),
        sha512("SHA-512");

        @NotNull
        private final ThreadLocal<MessageDigest> digestTL;

        Digest(@NotNull String algorithm) {

            this.digestTL = ThreadLocal.withInitial(() -> {
                try {
                    return MessageDigest.getInstance(algorithm);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @NotNull
        private String encode(@NotNull String value) {

            MessageDigest messageDigest = null;
            try {
                messageDigest = digestTL.get();
                messageDigest.update(value.getBytes(defaultValueEncoding));
                return hex2String(messageDigest.digest());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (messageDigest != null) {
                    messageDigest.reset();
                }
            }
        }
    }

    private static final String defaultValueEncoding = "UTF-8";
    private static char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    @NotNull
    public static String sha512Hex(@NotNull String value) {

        return Digest.sha512.encode(value);
    }

    @NotNull
    public static String md5Hex(@NotNull String value) {

        return Digest.md5.encode(value);
    }

    @NotNull
    private static String hex2String(byte[] bytes) {

        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte b : bytes) {
            buf.append(HEX_DIGITS[b >> 4 & 0x0f]);
            buf.append(HEX_DIGITS[b & 0x0f]);
        }
        return buf.toString();
    }
}
