package com.newtouch.uctp.module.business.util;

import com.qiyuesuo.sdk.v2.SaaSSdkClient;
import com.qiyuesuo.sdk.v2.bean.User;
import com.qiyuesuo.sdk.v2.http.StreamFile;
import com.qiyuesuo.sdk.v2.json.JSONUtils;
import com.qiyuesuo.sdk.v2.request.SaaSCompanyAuthResultRequest;
import com.qiyuesuo.sdk.v2.request.SaasCompanyAuthPageUrlRequest;
import com.qiyuesuo.sdk.v2.request.SaasPrivilegeUrlRequest;
import com.qiyuesuo.sdk.v2.response.CompanyAuthResult;
import com.qiyuesuo.sdk.v2.response.SaaSCompanyAuthPageResult;
import com.qiyuesuo.sdk.v2.response.SaaSPrivilegeUrlResult;
import com.qiyuesuo.sdk.v2.response.SdkResponse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class QIYueSuoSaasUtil {
//    APP Token （服务商接入令牌） yzFRkW26cb
//    APP Secret （服务商接入秘钥） eiTI3RA2yaMBpwmCAZPiXmzewEZstT
//    Secret            cJmnww1NgTUJwjvE

    public static String serverUrl = "https://openapi.qiyuesuo.cn"; // 测试环境域名为https://openapi.qiyuesuo.cn，生产环境域名为https://openapi.qiyuesuo.com
    public static String agentToken = "yzFRkW26cb"; // 由契约锁提供
    public static String agentSecret = "eiTI3RA2yaMBpwmCAZPiXmzewEZstT";

    public static void test1(){
        SaaSSdkClient sdkClient = new SaaSSdkClient(agentToken, agentSecret, serverUrl);
        try {
            SaasCompanyAuthPageUrlRequest urlRequest = new SaasCompanyAuthPageUrlRequest();
            urlRequest.setCompanyName("平头哥测试公司");
//        urlRequest.setRegisterNo("53453135123123123");
            urlRequest.setLegalPerson("平头哥");
            urlRequest.setApplicantInfo("{\"name\":\"平头哥\",\"contact\": \"17396202169\",\"contactType\": \"MOBILE\"}");

            urlRequest.setLicense(new StreamFile("营业执照", new FileInputStream("d:/R-C.jpg")));

//        urlRequest.setCallbackUrl("http://test.qiyuesuo.cn/...");
            String response = sdkClient.service(urlRequest);
            SdkResponse<SaaSCompanyAuthPageResult> responseObject = JSONUtils.toQysResponse(response, SaaSCompanyAuthPageResult.class);
            if (responseObject.getCode().equals(0)) {
                String format = String.format("生成链接成功，requestId:%s , 链接：%s",
                        responseObject.getResult().getRequestId(), responseObject.getResult().getPageUrl());
                System.out.println(format);
//            logger.info("生成链接成功，requestId:{} , 链接：{}",
//                    responseObject.getResult().getRequestId(), responseObject.getResult().getPageUrl());
            } else {
//            logger.info("生成链接失败，失败原因：" + responseObject.getMessage());
                System.out.println("生成链接失败，失败原因：" + responseObject.getMessage());
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void test2(){
        SaaSSdkClient sdkClient = new SaaSSdkClient(agentToken, agentSecret, serverUrl);
        SaaSCompanyAuthResultRequest request = new SaaSCompanyAuthResultRequest();
        request.setCompanyName("平头哥测试公司");
        // request.setRequestId("58d739b5-bf4f-4d2e-a2be-6cd0ba63a3c6");

        String response = sdkClient.service(request);
        SdkResponse<CompanyAuthResult> responseObject = JSONUtils.toQysResponse(response, CompanyAuthResult.class);
        if (responseObject.getCode().equals(0)) {
            String format = String.format("请求成功，状态为:%s", responseObject.getResult().getStatus());
            System.out.println(format);
            //请求成功，状态为:2
        } else {
            System.out.println("请求失败，失败原因：" + responseObject.getMessage());
        }

    }


    public static void test3(){
        SaaSSdkClient sdkClient = new SaaSSdkClient(agentToken, agentSecret, serverUrl);
        SaasPrivilegeUrlRequest urlRequest = new SaasPrivilegeUrlRequest();
        urlRequest.setCompanyId(2475607680987074766L);
        urlRequest.setUser(new User("17396202169", "MOBILE"));
        urlRequest.setCreateToken(true);
//        urlRequest.setCallbackUrl("http://test.qiyuesuo.cn/...");
//        urlRequest.setSuccessUrl("http://test.qiyuesuo.cn/...");
        List<String> privilegeModules = Arrays.asList("SEAL","TEMPLATE","CONTRACT","FILE_STATISTICS");
        String response = sdkClient.service(urlRequest);
        SdkResponse<SaaSPrivilegeUrlResult> responseObject = JSONUtils.toQysResponse(response, SaaSPrivilegeUrlResult.class);
        if (responseObject.getCode().equals(0)) {
            System.out.println("生成链接成功，链接：" + responseObject.getResult().getPageUrl());
        } else {
            System.out.println("生成链接失败，失败原因：" + responseObject.getMessage());
        }

    }

    public static void main(String[] args) {
        test3();

    }

}
