package com.lpb.mid.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompression {
    private static final Logger logger = LoggerFactory.getLogger(GzipCompression.class);

    public static String compressString(String input) {
        if(StringUtils.isBlank(input)) {
            return StringUtils.EMPTY;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (OutputStream gzipOs = new GZIPOutputStream(baos)) {
                gzipOs.write(input.getBytes(StandardCharsets.UTF_8));
            }
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            logger.error("Error when compress String: {}", ExceptionUtils.getStackTrace(e));
        }
        return input;
    }
    public static String encodeSha256(final String base) {
        try{

            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }

    }

    public static String decompressString(String compressedBase64) {
        if(StringUtils.isBlank(compressedBase64)) {
            return StringUtils.EMPTY;
        }
        try {
            byte[] compressedBytes = Base64.getDecoder().decode(compressedBase64);
            try (InputStream gzipIs = new GZIPInputStream(new ByteArrayInputStream(compressedBytes))) {
                byte[] decompressedBytes = new byte[1024];
                int len;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = gzipIs.read(decompressedBytes)) != -1) {
                    baos.write(decompressedBytes, 0, len);
                }
                return new String(baos.toByteArray(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            logger.error("Error when decompress String: {}", ExceptionUtils.getStackTrace(e));
        }
        return compressedBase64;
    }
}
