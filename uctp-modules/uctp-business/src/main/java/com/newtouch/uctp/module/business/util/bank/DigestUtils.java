package com.newtouch.uctp.module.business.util.bank;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Enumeration;

public class DigestUtils {

    //注册BouncyCastle,BouncyCastle为第三方库
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    // 算法名称参数,sm4算法
    public static final String ALGORITHM_NAME = "sm4";
    // SM4参数,PKCS5填充
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    /**
     * 默认USERID
     */
    public static String USER_ID = "1234567812345678";


    /**
     * 密钥加密
     *
     * @param algorithm 算法名称
     * @param content 密钥
     * @param charset 编码格式
     * @return
     */
    public static String keyDigest(String algorithm, String content, String charset) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(content.getBytes(charset));
            byte[] digestBytes = digest.digest();
            return DatatypeConverter.printHexBinary(digestBytes).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求报文体加密
     *
     * @param algorithm 算法名称
     * @param content 请求报文体
     * @param charset 编码格式
     * @return
     */
    public static String dataDigest(String algorithm, String content, String charset) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(content.getBytes(charset));
            byte[] digestBytes = digest.digest();
            return DatatypeConverter.printBase64Binary(digestBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param hash
     * @return
     */
    public static String md5Digest(String hash) {
        String md5Str = org.apache.commons.codec.digest.DigestUtils.md5Hex(hash);
        return md5Str;
    }

    /**
     *
     * 说明：sm3加密处理
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
     * P5填充加密
     *
     * @param key 密钥
     * @param data 请求报文体
     * @return
     */
    public static byte[] encrypt(byte[] key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING,
                    BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * P5填充解密
     *
     * @param key 密钥
     * @param signature 签名
     * @return
     */
    public static byte[] decrypt(byte[] key, byte[] signature) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING,
                    BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, sm4Key);
            return cipher.doFinal(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 签名
     *
     * @param key 密钥
     * @param data 请求报文体
     * @return
     */
    public static String sign(String key, String data) {
        try {
            String charset = "UTF-8"; //编码格式
            /**
             * 国密签名步骤：
             * 1)通过SHA-256生成X-SPDB-Client-Secret的摘要，转化成16进制，
             * 2)并通过SM3处理，得到sm3Hex
             * 3)通过md5对生成的sm3Hex做加密得到结果keyMD5
             * 4)通过SHA-1生成content的摘要，并转base64，把结果通过sm3做hash处理得到contentDigest
             * 5)使用keyMD5作为key，通过SM4对contentDigest进行加密，得到encryptData
             * 6)encryptData经Base64编码，得到最终的X-SPDB-SIGNATURE
             */
            String shaKey = keyDigest("SHA-256", key, charset);      //步骤1
            String sm3Key = sm3(shaKey);                                      //步骤2
            String sm4Key = md5Digest(sm3Key);                                //步骤3
            String sm4Data = sm3(dataDigest("SHA-1", data, charset));//步骤4

            byte[] keyBytes = ByteUtils.fromHexString(sm4Key);
            byte[] dataBytes = sm4Data.getBytes(charset);
            byte[] encryptBytes = encrypt(keyBytes, dataBytes);                //步骤5
            String hexSignature = ByteUtils.toHexString(encryptBytes).toUpperCase();
            byte[] signBytes = hexSignature.getBytes(charset);
            return DatatypeConverter.printBase64Binary(signBytes);             //步骤6
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *文件上传、大报文
     *获取MD5加密
     * @param filePath  文件的路径 数组
     */
    public static String fileMD5(String[] filePath) {
        String filesMD5 = "";

        for(int i = 0;i < filePath.length;i++){
            try {
                String fileMD5 = org.apache.commons.codec.digest.DigestUtils
                        .md5Hex(new FileInputStream(filePath[i]));
                if (filesMD5 == "") {
                    filesMD5 = fileMD5;
                } else {
                    filesMD5 = filesMD5 + "," + fileMD5;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filesMD5;
    }

    /**
     *大报文
     *获取MD5加密
     * @param requstBody  请求体
     */
    public static String bodyMD5( String requstBody){

        //RequestBody requstBody = builder.build();
        String bodyMD5 = "";
        bodyMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(requstBody);
        return bodyMD5;
    }




    /**
     * 验证签名(验签)
     *
     * @param publicKey 公钥信息
     * @param sourceData 密文信息
     * @param signData 签名信息
     * @return 验签的对象 包含了相关参数和验签结果
     */
    @SuppressWarnings("unchecked")
    public static SM2Sign validateSign(byte[] publicKey, byte[] sourceData, byte[] signData) {
        try {
            byte[] formatedPubKey;
            SM2Sign verifyVo = new SM2Sign();
            verifyVo.setSm2_type("verify");
            if (publicKey.length == 64) {
                // 添加一字节标识，用于ECPoint解析
                formatedPubKey = new byte[65];
                formatedPubKey[0] = 0x04;
                System.arraycopy(publicKey, 0, formatedPubKey, 1, publicKey.length);
            } else {
                formatedPubKey = publicKey;
            }
            SM2Factory factory = SM2Factory.getInstance();
            ECPoint userKey = factory.ecc_curve.decodePoint(formatedPubKey);

            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = factory.sm2GetZ(USER_ID.getBytes(), userKey);
            verifyVo.setSm3_z(SM2Util.getHexString(z));
            sm3Digest.update(z, 0, z.length);
            sm3Digest.update(sourceData, 0, sourceData.length);
            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            verifyVo.setSm3_digest(SM2Util.getHexString(md));

            ByteArrayInputStream bis = new ByteArrayInputStream(signData);
            ASN1InputStream dis = new ASN1InputStream(bis);
            SM2Result sm2Result = null;
            ASN1Primitive derObj = dis.readObject();
            dis.close();
            bis.close();
            Enumeration<ASN1Integer> e = ((ASN1Sequence) derObj).getObjects();
            BigInteger r = ((ASN1Integer) e.nextElement()).getValue();
            BigInteger s = ((ASN1Integer) e.nextElement()).getValue();
            sm2Result = new SM2Result();
            sm2Result.r = r;
            sm2Result.s = s;
            verifyVo.setVerify_r(sm2Result.r.toString(16));
            verifyVo.setVerify_s(sm2Result.s.toString(16));
            factory.sm2Verify(md, userKey, sm2Result.r, sm2Result.s, sm2Result);
            boolean verifyFlag = sm2Result.r.equals(sm2Result.R);
            verifyVo.setVerify(verifyFlag);
            return verifyVo;
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
