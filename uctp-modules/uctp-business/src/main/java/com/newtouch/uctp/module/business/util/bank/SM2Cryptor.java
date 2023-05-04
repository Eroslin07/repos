package com.newtouch.uctp.module.business.util.bank;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * 报文加解密工具类
 * 
 */
public class SM2Cryptor {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 算法名称
    public static final String ALGORITHM_NAME = "sm4";

    // P5填充
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";

    /**
     * SHA256加密
     * 
     * @param str
     * @return
     */
    public static String sha256(String str) {
        MessageDigest messageDigest = null;
        String enencdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            enencdeStr = Hex.encodeHexString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enencdeStr;
    }

    /**
     * MD5加密
     * 
     * @param hash
     * @return
     */
    public static String md5(String hash) {
        String md5Str = DigestUtils.md5Hex(hash);
        return md5Str;
    }

    /**
     * 
     * sm3加密处理
     * 
     * @param data
     * @return 2019年11月26日
     *
     */
    public static String sm3(String data) {
        String charset = "UTF-8";
        String sm3Data = "";
        try {
            byte[] dataBytes = data.getBytes(charset);
            byte[] hashBytes = hash(dataBytes);
            sm3Data = ByteUtils.toHexString(hashBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sm3Data;
    }

    /**
     * 
     * 返回长度为32位的byte数组 生成对应的hash值
     * 
     * @param dataBytes
     * @return 2019年10月28日
     *
     */
    public static byte[] hash(byte[] dataBytes) {
        SM3Digest digest = new SM3Digest();
        digest.update(dataBytes, 0, dataBytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 报文体加密
     * 
     * @param key
     * @param data
     * @return
     */
    public static String encrypt(String key, String data) {

        String encrypted = "";

        try {
            String charset = "UTF-8";

            String sha256Key = sha256(key);
            String sm3Key = sm3(sha256Key);
            String md5Key = md5(sm3Key);

            byte[] keyBytes = ByteUtils.fromHexString(md5Key);
            byte[] dataBytes = data.getBytes(charset);

            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING,
                    BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec sm4Key = new SecretKeySpec(keyBytes, ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
            byte[] encryptBytes = cipher.doFinal(dataBytes);
            String hexSignature = ByteUtils.toHexString(encryptBytes).toUpperCase();
            byte[] signBytes = hexSignature.getBytes(charset);
            encrypted = DatatypeConverter.printBase64Binary(signBytes);
            return encrypted;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    /**
     * 报文体解密
     * 
     * @param key
     * @param signature
     * @return
     */
    public static String decrypt(String key, String encrypted) {

        String decrypted = "";

        try {

            String sha256Key = sha256(key);
            String sm3Key = sm3(sha256Key);
            String md5Key = md5(sm3Key);
            byte[] keyBytes = ByteUtils.fromHexString(md5Key);

            byte[] encryptBytes = DatatypeConverter.parseBase64Binary(encrypted);
            String hexSignature = new String(encryptBytes).toLowerCase();
            byte[] cipherBytes = ByteUtils.fromHexString(hexSignature);

            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING,
                    BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec sm4Key = new SecretKeySpec(keyBytes, ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, sm4Key);
            byte[] doFinal = cipher.doFinal(cipherBytes);
            decrypted = new String(doFinal);
            return decrypted;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

}
