package com.newtouch.uctp.module.business.util;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SignatureUtils {
    public static final String FIELD_SIGN = "sign";
    public static final String MD5 = "MD5";
    private static final String FIELD_KEY = "key";

    /**
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String generateSignature(final Map data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(FIELD_SIGN) || k.equals(FIELD_KEY)) {
                continue;
            }
            Object objValue = data.get(k);
            if (objValue.getClass().isArray()) {
                Object[] arryObj = (Object[]) objValue;
                objValue = Arrays.toString(arryObj).replace(" ","");
            }
            if (objValue.toString().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(objValue.toString().trim()).append("&");
        }
        sb.append("key=").append(key);
        return MD5(sb.toString()).toUpperCase();
    }



    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance(MD5);
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key  API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        if (!data.containsKey(FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("channel", "tywg");
        param.put("brandId", 1);
        System.out.println(SignatureUtils.generateSignature(param, "B82pho2ts3HPTHvfg8JaU7cB322kTxDE"));
    }


}
