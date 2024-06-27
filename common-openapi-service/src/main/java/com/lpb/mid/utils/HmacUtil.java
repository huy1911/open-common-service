package com.lpb.mid.utils;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class HmacUtil {

    public static String genHmacSHA256WithEncode(List<String> data, String key) {
        Digest digest = new SHA256Digest();
        HMac hMac = new HMac(digest);
        hMac.init(new KeyParameter(key.getBytes()));

        String dataIn = data.stream().map(String::valueOf).collect(Collectors.joining("-"));
        byte[] hmacIn = dataIn.getBytes();
        hMac.update(hmacIn, 0, hmacIn.length);

        byte[] hmacOut = new byte[hMac.getMacSize()];
        hMac.doFinal(hmacOut, 0);
        new String(Hex.encodeHex(hmacOut));
        return Base64.getEncoder().encodeToString(hmacOut);

    }
    public static String genHmac(String message,String serviceKey){
        String encodeRequest = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(serviceKey.getBytes(), "HmacSHA256"));
            byte[] result = mac.doFinal(message.getBytes());
            encodeRequest = new String(Hex.encodeHex(result));

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return encodeRequest;
    }

    public static String encrypt(String strToEncrypt, String secretKey, String salt) {

        try {

            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), Constants.ITERATION_COUNT, Constants.KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

            byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            byte[] encryptedData = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            // Handle the exception properly
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String strToDecrypt, String secretKey, String salt) {

        try {

            byte[] encryptedData = Base64.getDecoder().decode(strToDecrypt);
            byte[] iv = new byte[16];
            System.arraycopy(encryptedData, 0, iv, 0, iv.length);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), Constants.ITERATION_COUNT, Constants.KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);

            byte[] cipherText = new byte[encryptedData.length - 16];
            System.arraycopy(encryptedData, 16, cipherText, 0, cipherText.length);

            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText, "UTF-8");
        } catch (Exception e) {
            // Handle the exception properly
            e.printStackTrace();
            return null;
        }
    }

}
