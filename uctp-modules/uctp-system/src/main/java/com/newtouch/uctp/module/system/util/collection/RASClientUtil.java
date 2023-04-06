package com.newtouch.uctp.module.system.util.collection;

import com.xingyuv.captcha.util.Base64Utils;

import javax.crypto.Cipher;
import java.net.URLDecoder;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RASClientUtil {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJaHc3K37lCpRfDWodcziTuWr3x/3ADiJCAorUsHW2vi4iAv92WzQuQGBjihL8OfcnSoeSlRORNPzi4wd7LtzB132T3T/rlyl1rBfQnQo7dLdnCX13kAm3v+F1JqncBwfDd2+Wtm+8MnvYBpHdhQ+6M0zd2EsLu2x0NvcLGglIENAgMBAAECgYAsR/ZXRfJOOi1/9rOvSdLR+7bt6fL/M4crCqxHyQdEyn54t4OQoFZKG9eSqyAQ7QPPe4wA8orWuoBNqCZeNYP4pXV2ayPwZcUSN9SX4/ce5QZkhHDVBwC8SIQQ7osU6Joh4gR3I+CHlmM1dCItBizOC0Jw4Scs7cpnzzMgYhdPoQJBAO9gzFvGBROOMwtqmOU7adbbM8FE8LRHnRrKv6OvX3Qs5Kqu4vFY78LW4tPzxbzAdEMAF9rltPcc3Y9D8U8Am7UCQQCg+0Q/Za+HQ5Tgbv9QGYI1tvTUe6WiC3VHcUGmQIqa78baEd7pndcPZuqbnAPVw4oWsuhQEXSakuL+KLGJXZb5AkBE2sANidj99gIiv4e5MCzSe3zYk970zECZa0ZSa+h1/0/K9MEckOtuTOcz9kOjdmw6tXUnJrm19tyYEAACLHedAkAyrATmg8aFqFMzdhzthKoE6GsWezk+0aZ/73l/sG8wp+sK93cYSDPKyFVu1+QpJFzSGkyf726pvTSwVfTUTV5ZAkBWX+yR7VdY3e55rQBQg8k0XhFcldbaN1rZz+a41+smvpxwlslxI+ERH1yY2COUxoZIiD9VhGWudvjca+0tRgXA";
    private static  volatile ConcurrentHashMap<String,Key> KEY_MAP  = new ConcurrentHashMap<>(2);


    /**
     * base64解码
     *
     * @param content
     * @return byte[]
     * @author compass
     * @date 2022/9/1 17:12
     * @since 1.0.0
     **/
    public static byte[] decryptBASE64(String content) {
        return org.springframework.util.Base64Utils.decode(content.getBytes());
    }

    /**
     * base64编码
     *
     * @param bytes 字符byte数组
     * @return java.lang.String
     * @author compass
     * @date 2022/9/1 17:12
     * @since 1.0.0
     **/
    public static String encryptBASE64(byte[] bytes) {
        return new String(org.springframework.util.Base64Utils.encode(bytes));
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return java.lang.String
     * @author compass
     * @date 2022/9/1 14:21
     * @since 1.0.0
     **/
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    /**
     * 通过私钥解密
     *
     * @param data       加密的byte数组
     * @return byte[]
     * @author compass
     * @date 2022/9/1 17:14
     * @since 1.0.0
     **/
    public static byte[] decryptByPrivateKey(byte[] data)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(PRIVATE_KEY);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privatizationKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privatizationKey);
        return cipher.doFinal(data);

    }

    /**
     * 私钥解密前端jsencrypt分段加密的长文本内容,
     * 前端加密的字符串需要使用encodeURIComponent编码,后端使用URLDecoder.decode解码来解决中文字符串无法解密的问题
     *
     * @param content    加密的内容
     * @return java.lang.String
     * @author compass
     * @date 2022/9/1 16:53
     * @since 1.0.0
     **/
    public static String jsencryptDecryptByPrivateKeyLong(String content) {
        String resultContent = "";
        try {
            StringBuffer buffer = new StringBuffer();
            if (content != null && content.trim().length() > 0) {
                String[] contentList = content.split("=");
                for (String item : contentList) {
                    byte[] itemBytes = Base64Utils.decode((item + "=").getBytes());
                    try {
                        byte[] itemResultBytes = decryptByPrivateKey(itemBytes);
                        String itemResultStr = new String(itemResultBytes);
                        buffer.append(itemResultStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            resultContent = URLDecoder.decode(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("jsencryptDecryptByPrivateKeyLong解密出错:" + e.getMessage() + ":" + "解密内容:" + content);
            throw new RuntimeException("rsa解密失败");
        }
        return resultContent;
    }


    /**
     * 通过私钥解密
     *
     * @param data       加密的byte数组
     * @return byte[]
     * @author compass
     * @date 2022/9/1 17:14
     * @since 1.0.0
     **/
    public static byte[] decryptByPrivateKey(String data)
            throws Exception {
        return decryptByPrivateKey(decryptBASE64(data));
    }

    /**
     * 解密<br>
     *
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    /**
     * 用公钥解密
     *
     * @param data      代解密的byte数组
     * @param publicKey
     * @return byte[]
     * @author compass
     * @date 2022/9/1 17:17
     * @since 1.0.0
     **/
    public static byte[] decryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(publicKey);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicityKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicityKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 获取私钥
     *
     * @param keyMap 存放私钥和公钥的map集合
     * @return java.lang.String
     * @author compass
     * @date 2022/9/1 17:18
     * @since 1.0.0
     **/
    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key =  keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap 放私钥和公钥的map集合
     * @return java.lang.String
     * @author compass
     * @date 2022/9/1 17:28
     * @since 1.0.0
     **/
    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化公钥和秘钥 每次调用可以获取不同的公钥和私钥
     * @return java.util.Map<java.lang.String, java.security.Key>
     * @author compass
     * @date 2022/9/1 17:28
     * @since 1.0.0
     **/
    public static ConcurrentHashMap<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        ConcurrentHashMap<String, Key> keyMap = new ConcurrentHashMap<>(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
        return keyMap;
    }


    public static void main(String[] args) throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWh3Nyt+5QqUXw1qHXM4k7lq98f9wA4iQgKK1LB1tr4uIgL/dls0LkBgY4oS/Dn3J0qHkpUTkTT84uMHey7cwdd9k90/65cpdawX0J0KO3S3Zwl9d5AJt7/hdSap3AcHw3dvlrZvvDJ72AaR3YUPujNM3dhLC7tsdDb3CxoJSBDQIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJaHc3K37lCpRfDWodcziTuWr3x/3ADiJCAorUsHW2vi4iAv92WzQuQGBjihL8OfcnSoeSlRORNPzi4wd7LtzB132T3T/rlyl1rBfQnQo7dLdnCX13kAm3v+F1JqncBwfDd2+Wtm+8MnvYBpHdhQ+6M0zd2EsLu2x0NvcLGglIENAgMBAAECgYAsR/ZXRfJOOi1/9rOvSdLR+7bt6fL/M4crCqxHyQdEyn54t4OQoFZKG9eSqyAQ7QPPe4wA8orWuoBNqCZeNYP4pXV2ayPwZcUSN9SX4/ce5QZkhHDVBwC8SIQQ7osU6Joh4gR3I+CHlmM1dCItBizOC0Jw4Scs7cpnzzMgYhdPoQJBAO9gzFvGBROOMwtqmOU7adbbM8FE8LRHnRrKv6OvX3Qs5Kqu4vFY78LW4tPzxbzAdEMAF9rltPcc3Y9D8U8Am7UCQQCg+0Q/Za+HQ5Tgbv9QGYI1tvTUe6WiC3VHcUGmQIqa78baEd7pndcPZuqbnAPVw4oWsuhQEXSakuL+KLGJXZb5AkBE2sANidj99gIiv4e5MCzSe3zYk970zECZa0ZSa+h1/0/K9MEckOtuTOcz9kOjdmw6tXUnJrm19tyYEAACLHedAkAyrATmg8aFqFMzdhzthKoE6GsWezk+0aZ/73l/sG8wp+sK93cYSDPKyFVu1+QpJFzSGkyf726pvTSwVfTUTV5ZAkBWX+yR7VdY3e55rQBQg8k0XhFcldbaN1rZz+a41+smvpxwlslxI+ERH1yY2COUxoZIiD9VhGWudvjca+0tRgXA";

        String inputStr = "sign";
        byte[] data = inputStr.getBytes();
        byte[] encodedData = encryptByPrivateKey(data, privateKey);
//        String con = "F41sMcJ0umNgZcFzxIzoLY8eZP8vwE5QNq/slHOuft63bWZwANaK8bvHNtac7/qvohEeP2BBPb80wYtToJGSsUegPu2fIlWoyA+JBVDNHRItJNwwNCgbhnBkUR2T8NGRrpjLHoA8dmAh0l9uaoCDog5ZwvHEOLSzTILvY4Rei2U=gB15njJfeQqbNJe2qQ3ui6J88vyjHDL4FKw3KWbY2iXyQM/b+RqT9kgnb0nDhAuZkJ5BWTQorpmyiAQYHaQmz5JgDpZCyw8E4XOXPgZXKgLXkEXwLlhwOhhoH5QsqCieqzu2pG6CcGeYycU+yy2fSs1VPRNIfPpeHde76ZH3urg=YRw5Y0abdEmoaLk0iz44nOPxrO4qBrIBvtBTeAQWv5Dme6YFXOxNIFd63FpE0o5c3nplueDJF17bNbIVT+rAkb54mR/jRXygvosach2ONlM/kw6UaV5lQ+BAAt/+71I7wGqxTvyxz1UFWmIIiYszQxxWIhUxH3hYXY7uHTVOg18=MMLZEy3iNkhZiPuEoLcjRKgVB6HnEY9ywVffY3B/ZBQhUqrONwkjfoh8k5w+xhT7VhMStKCxgfzNNRmKCxvxlLY86nGkZ9l9pkGJqfCQwJSNSP1eYszafgtoOn6/Rc7znPoxxhJ0AWmlcUEB+8PnePO9ApAKai0Jdx4KbpbC7Fg=";
        String con = "C+tUb3XKolNHaZ3viha8XYifdkYigTnfz+VQ8wzRO2/zms7q0EUKy1BOHY7FZ4am19gBMG0tOy9aNUJtG1d8eJqvHf6xCsEWOYOh3/T0W6dpoo+VbaoF2XEoKXcQCusmitteSBJc0Lr4lbAxoZb5QvdUydDQB2YlDdPc7w0ses8=";
//        byte[] decodedData = RSACode.decryptByPrivateKey(Base64Utils.decode(con.getBytes()), privateKey);
        String result = jsencryptDecryptByPrivateKeyLong(con);

        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + result);

        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = sign(encodedData, privateKey);
        System.err.println("签名:" + sign);
        // 验证签名
        boolean status = verify(encodedData, publicKey, sign);
        System.err.println("状态:" + status);
    }
}
