package com.newtouch.uctp.module.business.util;

/*
 * 车辆信息查询工具类
 * CarFairValue->公允价值查询
 * CarMotorcycleType->车型查询
 *
 * @author Mr Li
 * @date 20230408
 *
 *
 * */


import cn.hutool.log.Log;
import cn.hutool.log.dialect.log4j2.Log4j2Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class UctpCarInfoSearchUtils {
    Log log = new Log4j2Log(UctpCarInfoSearchUtils.class);

    /**
     * GET请求
     *
     * @param modelId      modelId (⻋型ID) 必传
     * @param zone         zone(城市标识ID/全国统⼀⾏政代码) 与carNo二选一
     * @param mile         mile(⻋辆⾏驶⾥程，单位是万公⾥) 必传
     * @param regDate      regDate(⻋辆上牌年-⽉或年-⽉-⽇) 必传
     * @param allLevel     allLevel(是否返回多⻋况,1是，0:否) 默认1
     * @param url          url （接口地址） 必传
     * @param token        token(⻋300提供给客户的调⽤帐号,最长32位) 必传
     * @param coefficients coefficients车价浮动系数 默认0.2
     */
    public HashMap CarFairValue(String modelId, String zone, String mile, String regDate, String allLevel, String token, String url, String coefficients) {
//        url = "http://testapi.che300.com/service/getUsedCarPrice";
//        token = "61f499b086392005f92009b91f8f966a";
        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?token=" + token);
        urlParam.append("&modelId=" + modelId);
        urlParam.append("&zone=" + zone);
        urlParam.append("&mile=" + mile);
        urlParam.append("&regDate=" + regDate);
        urlParam.append("&allLevel=" + allLevel);
        String getUrl = urlParam.toString();
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(getUrl);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");
        HashMap mapinfo = new HashMap<>();

        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
            Map maps = JSON.parseObject(result);
            // model_price 新⻋指导价
            String model_price = "";
            //dealer_low_sold_price 最低⻋商零售价
            String dealer_low_sold_price = "";
            //dealer_high_sold_price 最⾼⻋商零售价
            String dealer_high_sold_price = "";
            // Recommended_low_sold_price 推荐最低车商零售价
            String Recommended_low_sold_price = null;
            // Recommended_high_sold_price 推荐最高车商零售价
            String Recommended_high_sold_price = null;
            //转换系数类型  默认为0.2,
            Double coefficient = new Double(StringUtil.isEmpty(coefficients) ? "0.2" : coefficients);
            for (Object map : maps.entrySet()) {
                if (((Map.Entry) map).getKey().equals("model_price")) {
                    //指导价
                    model_price = ((Map.Entry) map).getValue().toString();
                } else if (((Map.Entry) map).getKey().equals("eval_prices")) {
                    JSONArray jsonArray = (JSONArray) ((Map.Entry) map).getValue();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.get("condition").equals("excellent")) {
                            dealer_high_sold_price = jsonObject.get("dealer_high_sold_price").toString();
                        } else if (jsonObject.get("condition").equals("normal")) {
                            dealer_low_sold_price = jsonObject.get("dealer_low_sold_price").toString();
                        }
                    }
//                    System.out.println("dealer_high_sold_price------" + dealer_high_sold_price);
//                    System.out.println("dealer_low_sold_price----------" + dealer_low_sold_price);
                }
            }
            //  add():加法， subtract():减法:, multiply():乘法; divide():除法，
            Recommended_low_sold_price = String.format("%.2f", new BigDecimal(dealer_low_sold_price).multiply(new BigDecimal(1 - coefficient)));
            Recommended_high_sold_price = String.format("%.2f", new BigDecimal(dealer_high_sold_price).multiply(new BigDecimal(1 + coefficient)));
            //            System.out.println("model_price" + "------" + model_price);
            mapinfo.put("dealer_low_sold_price", dealer_low_sold_price);
            mapinfo.put("dealer_high_sold_price", dealer_high_sold_price);
            mapinfo.put("Recommended_low_sold_price", Recommended_low_sold_price);
            mapinfo.put("Recommended_high_sold_price", Recommended_high_sold_price);
            mapinfo.put("model_price", model_price);
            mapinfo.put("msg", "success");

            getMethod.releaseConnection();
            return mapinfo;

        } catch (IOException e) {
            log.error("GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}", url, getUrl, e.getMessage(), e);
            mapinfo.put("msg", "GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}----" + url + "-----" + getUrl + "-----" + e.getMessage() + "-------" + e);
            return mapinfo;
        }
    }

    /**
     * GET请求
     *
     * @param fromVersion fromVersion (版本号,最长10)
     * @param simple      simple(是否是简洁模式,1:是，0:否，默认 0，简洁模式 model 只返回车型 id,防止由于网关限制报文大小导致获取数据失败
     * @param apiVersion  apiVersion（传固定值:v2）
     * @param url         url （接口地址）
     * @param token       token(⻋300提供给客户的调⽤帐号,最长32位)
     *                    )
     */
    public void CarMotorcycleType(String url, String token, String apiVersion, String fromVersion, String simple) {
//        url = "http://testapi.che300.com/service/pv/exportModel";
//        token = "61f499b086392005f92009b91f8f966a";
        apiVersion = "v2";
        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?token=" + token);
        urlParam.append("&fromVersion=" + fromVersion);
        urlParam.append("&simple=" + simple);
        urlParam.append("&apiVersion=" + apiVersion);
        String getUrl = urlParam.toString();
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(getUrl);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();

            getMethod.releaseConnection();
        } catch (IOException e) {
            log.error("GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}", url, getUrl, e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String url1 = "http://testapi.che300.com/service/getUsedCarPrice";
        String url2 = "http://testapi.che300.com/service/pv/exportModel";
        String token = "61f499b086392005f92009b91f8f966a";
        String modelId = "1833853";
        String zone = "28";
        String mile = "4.0";
        String regDate = "2023-03-01";
        String allLevel = "1";
        String coefficients = "0.1";
        UctpCarInfoSearchUtils uctpCarInfoSearchUtils = new UctpCarInfoSearchUtils();
//        uctpCarInfoSearchUtils.CarMotorcycleType("3.4.561", "0");
        HashMap map = uctpCarInfoSearchUtils.CarFairValue(modelId, zone, mile, regDate, allLevel, token, url1, coefficients);
        System.out.println(map.toString());
    }
}
