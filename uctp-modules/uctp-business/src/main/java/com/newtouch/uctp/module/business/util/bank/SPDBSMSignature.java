package com.newtouch.uctp.module.business.util.bank;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SPDBSMSignature {

    /*ClientID和密钥：X-SPDB-ClientID、X-SPDB-ClientID-Secret
     * 浦发API开发平台上创建APP给您邮件返回的，或我们提供给您测试使用的
     * 生产请使用API开发平台上创建APP给您邮件返回的生产环境密钥
     */
    private static final String clientId = "0010d65a-1a3e-4a82-8b32-47a7cd2783dc";
	//加解密样例仅供参考，秘钥有过期时间，建议在生产环境不要将秘钥硬编码在代码中
    private static final String secret = "MTPhMC00NzA3LTg1MTctZWVzZDAzNmJjOWJ3MC43MDP0NTM0MzczNDUxMjQ4MC45";

    /*
     *请求头ContentType,一般为application/json,API对外接口文档中有报文体格式
     *图文上传接口为form-data,如果是图文信息上传,请参考国密普通验签图片上传接口测试工程.zip
     */
    private static final String contentType = "application/json;charset=utf-8";

    /*
	 *浦发公钥，在用户中心我的APP开发者公钥中查看
	 *加解密样例仅供参考，秘钥有过期时间，建议在生产环境不要将秘钥硬编码在代码中
     */
    private static final String spdbPublicKey = "04b3623718d2b206f3819e813687deba9362ed6a47dc514a9d103113706137a10cfcb7ded82e0b8e1de7b797d49c19e2f00f85b3b0144f69ac0d3e76206517cdd5";
    /**
     * 请求编码格式
     */
    private static final String charset = "UTF-8";


    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(500, 5, TimeUnit.MINUTES)).connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS).build();


    /**
     * 获取防重放参数
     * @param forbidden  是否需使用防重放参数
     * @param data   报文体
     * @return
     */
    public static String getNonceParam( boolean forbidden ,String data){
        //防重放参数。毫秒级的时间戳，与浦发服务器（北京时间）时间误差不能超过15分钟。样例：1543461367533
        long timestamp = 0L;
        //防重放参数。X-SPDB-Nonce为交易序号，需保证唯一。样例：TRANS10145581
        String nonce = "";
        /*
         * 防重放参数
         * 如需使用防重放参数，需要对‘时间戳’和‘防重放参数’加签
         */
        String newBodyData = data;

        if (forbidden) {//如需使用防重放参数
            //对应请求报文头参数 X-SPDB-Timestamp
            timestamp = System.currentTimeMillis();// X-SPDB-Timestamp
            //对应请求报文头参数 X-SPDB-Nonce
            nonce = "TRAN10145581";// X-SPDB-Nonce
            //注意：签名原文需要拼接防重放参数 例：{"name":"zhangshan"}1591077686410TRAN10145581
            newBodyData = data + timestamp + nonce;
            System.out.println("签名原文:" + newBodyData);
        }
        return newBodyData;
    }

    /**
     * 获取签名
     * @param newBodyData   报文参数
     * @param isUpFile  是否是文件上传
     * @param isBody   是否是文件上传
     * @return
     */
    public static String getSign(String newBodyData, boolean isUpFile,boolean isBody){
        String bodyMD5 = "";
        String fileDate = "";
        String sign = "";
        if(isUpFile){
            String[] filePath = {"c:/content.jpg"};
            fileDate = DigestUtils.fileMD5(filePath);
            sign = DigestUtils.sign(secret,fileDate);
            System.out.println("X-SPDB-FilesMD5: "+ fileDate);
            System.out.println("X-SPDB-ClientID-Secret："+secret);
            System.out.println("X-SPDB-SIGNATURE:" + sign);//签名结果
            System.out.println("————————文件上传签名结束————————");
        }else if(isBody){//是否为大报文
            bodyMD5 = DigestUtils.bodyMD5(newBodyData);
            sign = DigestUtils.sign(secret,bodyMD5);
            System.out.println("X-SPDB-BodyMD5: "+ bodyMD5);
            System.out.println("原报文体："+newBodyData);
            System.out.println("X-SPDB-ClientID-Secret："+secret);
            System.out.println("X-SPDB-SIGNATURE:" + sign);//签名结果
            System.out.println("————————大报文签名结束————————");
        }else{
            System.out.println("原报文体:" + newBodyData);
            sign = DigestUtils.sign(secret, newBodyData);//签名
            System.out.println("X-SPDB-SIGNATURE:" + sign);//签名结果
        }
        return sign;
    }


    public static Request createRequest(String requestMethod,String url,String data,String signature){
        Request request =null;
        if("GET".equals(requestMethod) || "get".equals(requestMethod)){
            url = url + "?" + data;
            request = new Request.Builder().url(url).get()
                    .addHeader("Content-Type", contentType)
                    .addHeader("X-SPDB-Client-ID", clientId)
                    .addHeader("X-SPDB-SIGNATURE", signature)
                    .addHeader("X-SPDB-SM", "true")
                    //X-SPDB-Timestamp、X-SPDB-Nonce两个参数为防重放参数，不需要时不传
//                .addHeader("X-SPDB-Timestamp", String.valueOf(timestamp)) //防重放参数-毫秒级时间戳
//                .addHeader("X-SPDB-Nonce", nonce)//防重放参数
//  			  .addHeader("X-SPDB-BodyMD5", bodyMD5) //大报文
//  			  .addHeader("X-SPDB-FilesMD5", fileDate) //文件上传
                    .build();

        }else if("POST".equals(requestMethod) || "post".equals(requestMethod)){
            MediaType mediaType = MediaType.parse(contentType);
            RequestBody body = RequestBody.create(mediaType, data);
            request = new Request.Builder().url(url).post(body)
                    .addHeader("Content-Type", contentType)
                    .addHeader("X-SPDB-Client-ID", clientId)
                    .addHeader("X-SPDB-SIGNATURE", signature)
                    .addHeader("X-SPDB-SM", "true")
                    .addHeader("X-SPDB-Encryption", "true")
                    //X-SPDB-Timestamp、X-SPDB-Nonce两个参数为防重放参数，不需要时不传
//                .addHeader("X-SPDB-Timestamp", String.valueOf(timestamp)) //防重放参数-毫秒级时间戳
//                .addHeader("X-SPDB-Nonce", nonce)//防重放参数
//  			  .addHeader("X-SPDB-BodyMD5", bodyMD5) //大报文
//  			  .addHeader("X-SPDB-FilesMD5", fileDate) //文件上传
                    .build();
        }else{
            throw new NullPointerException("request is null");
        }
        return request;
    }

    /**
     * 调用银行接口方法
     * @param requestMethod 请求方法（get/post/...）
     * @param url 请求url
     * @param requestMessage 请求报文（json）
     * @return 响应报文(json)
     * @throws Exception
     */
    public static String call(String requestMethod, String url, String requestMessage) throws Exception {
        /**
         * 获取防重放参数
         */
        String newBodyData = getNonceParam(false, requestMessage);
        /**
         * 获取签名
         */
        String signature = getSign(newBodyData,false,false);

        //发送HTTP请求
        Request request = createRequest(requestMethod, url, requestMessage, signature);
        Response response = client.newCall(request).execute();

        String resBody = new String(response.body().bytes(), "UTF-8");
        log.info("响应报文：" + resBody);

        String sg = new String(response.header("X-SPDB-SIGNATURE").getBytes(), "UTF-8");

        String digest = SHA1.digest(resBody);
        byte[] bytes = digest.getBytes(charset);

        byte[] signatureBytes = ByteUtils.fromHexString(new String(DatatypeConverter.parseBase64Binary(sg)));
        SM2Sign sm2Sign = SM2SignVerify.validateSign(ByteUtils.fromHexString(spdbPublicKey), bytes, signatureBytes);

        boolean validateSign = sm2Sign.isVerify();
        log.info("验签结果：" + validateSign);
        if (!validateSign) {
            throw new RuntimeException("银行响应报文验签失败");
        }

        return resBody;
    }

    public static void main(String[] args) throws Exception {

        /**
         * 请求的URL,请使用对API外接口文档中提供的URL
         */
        String url = "https://etest4.spdb.com.cn/spdb/uat/api/fundDeposits/eDeposits/nominalAcccount";
        
        //请求类型
        String requestMethod = "post";
        
        /*
         * 请求报文体 {"name":"zhangshan"} 注意换行符和空格
         * 如果是图文信息上传，请参考国密普通验签图片上传接口测试工程.zip
         * GET请求报文传递，使用? 后面所有拼接的参数，
         * 多个参数：?name=zhangshan&age=18&addr=中国
         */
        String data = "{\"name\":\"zhangshan\"}";      
        /**
         * 获取防重放参数
         */
        String newBodyData = getNonceParam(false, data);
        /**
         * 获取签名
         */
        String signature = getSign(newBodyData,false,false);

        //发送HTTP请求
        Request request = createRequest(requestMethod,url,data,signature);
        Response response = client.newCall(request).execute();

        String resBody = new String(response.body().bytes(), "UTF-8");
        System.out.println("响应报文：" + resBody);

        String sg = new String(response.header("X-SPDB-SIGNATURE").getBytes(), "UTF-8");

        String digest = SHA1.digest(resBody);
        byte[] bytes = digest.getBytes(charset);

        byte[] signatureBytes = ByteUtils.fromHexString(new String(DatatypeConverter.parseBase64Binary(sg)));
        SM2Sign sm2Sign = SM2SignVerify.validateSign(ByteUtils.fromHexString(spdbPublicKey), bytes, signatureBytes);

        boolean validateSign = sm2Sign.isVerify();
        System.out.println("验签结果：" + validateSign);
    }

    /**
     *
     * @param url   请求的URL,请使用对API外接口文档中提供的URL
     * @param requestMethod   请求类型 GET/POST
     * @param param   请求参数
     * @param <T> cls   返回值类型
     * @return  请求返回值
     * @throws Exception
     */

    public static final String REQUEST_METHOD_GET = "get";
    public static final String REQUEST_METHOD_POST = "post";

    public static <T> T request (String url, String requestMethod, Object param, boolean forbidden, Class<T> cls)  {
        String resBody = "";

        String data = JSON.toJSONString(param);
        /**
         * 获取防重放参数
         */
        String newBodyData = getNonceParam(false, data);
        /**
         * 获取签名
         */
        String signature = getSign(newBodyData,false,false);

        //发送HTTP请求
        Request request = createRequest(requestMethod,url,data,signature);
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (response != null) {
                resBody = new String(response.body().bytes(), "UTF-8");
                String sg = new String(response.header("X-SPDB-SIGNATURE").getBytes(), "UTF-8");
                System.out.println("响应报文：" + resBody);


                String digest = SHA1.digest(resBody);
                byte[] bytes = digest.getBytes(charset);

                byte[] signatureBytes = ByteUtils.fromHexString(new String(DatatypeConverter.parseBase64Binary(sg)));
                SM2Sign sm2Sign = SM2SignVerify.validateSign(ByteUtils.fromHexString(spdbPublicKey), bytes, signatureBytes);

                boolean validateSign = sm2Sign.isVerify();
                System.out.println("验签结果：" + validateSign);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JSON.parseObject(resBody,cls);

    }
}