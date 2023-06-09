package com.newtouch.uctp.module.system.util.collection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class OCRUtil {

    public static String getAuth(String ak, String sk) {
        //ak sk 测试
//         ak="dzb9KhZMmaTdGd3rbnmXnc0u";
//         sk="K65GO95WOMOyloXZ4ZV72MKEX9EreG5H";
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = JSON.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }


    /**
     * 识别身份证
     * @param filePath
     * @param accessToken
     * @return
     */
    public static String idcard(String filePath,String accessToken) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
        String prePath="";
        try {

//            byte[] imgData = FileUtil.readFileByBytes(filePath);
//            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(filePath, "UTF-8");

            String param = "id_card_side=" + "front" + "&image=" + imgParam;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 识别营业执照
     * @return
     */
    public static String businessLicense(String filePath,String accessToken) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";
        try {
//            byte[] imgData = FileUtil.readFileByBytes(filePath);
//            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(filePath, "UTF-8");

            String param = "image=" + imgParam;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 识别行驶证
     * @return
     */
    public static String vehicleLicense(String filePath,String accessToken) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license";
        try {
            String imgParam = URLEncoder.encode(filePath, "UTF-8");

            String param = "image=" + imgParam;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 识别登记证书
     * @return
     */
    public static String vehicleRegistrationCertificate(String filePath,String accessToken) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_registration_certificate";
        try {
            String imgParam = URLEncoder.encode(filePath, "UTF-8");

            String param = "image=" + imgParam;

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
