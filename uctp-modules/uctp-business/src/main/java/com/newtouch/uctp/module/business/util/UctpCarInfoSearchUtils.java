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
import com.newtouch.uctp.framework.common.util.object.ObjectUtils;
import jodd.util.StringUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UctpCarInfoSearchUtils {
    Log log = new Log4j2Log(UctpCarInfoSearchUtils.class);

    /**
     * GET请求
     *
     * @param modelId      modelId (⻋型ID) 必传
     * @param carNo        carNo(⻋牌号)  必传
     * @param mile         mile(⻋辆⾏驶⾥程，单位是万公⾥) 必传
     * @param regDate      regDate(⻋辆上牌年-⽉或年-⽉-⽇) 必传
     * @param allLevel     allLevel(是否返回多⻋况,1是，0:否) 默认1
     * @param url          url （接口地址） 必传
     * @param token        token(⻋300提供给客户的调⽤帐号,最长32位) 必传
     * @param coefficients coefficients车价浮动系数 默认0.2
     */
    public HashMap CarFairValue(String modelId, String carNo, String mile, String regDate, String allLevel, String token, String url, String coefficients) throws UnsupportedEncodingException {
//        url = "http://testapi.che300.com/service/getUsedCarPrice";
//        token = "61f499b086392005f92009b91f8f966a";
        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?token=" + token);
        urlParam.append("&modelId=" + modelId);
        urlParam.append("&carNo=" + carNo);
        urlParam.append("&mile=" + mile);
        urlParam.append("&regDate=" + regDate);
        urlParam.append("&allLevel=" + allLevel);
        //处理url中中文编码
        Matcher matcher = Pattern.compile("[\u4e00-\u9fa5]").matcher(urlParam);
        String getUrl = null;
        String uri = urlParam.toString();

        while (matcher.find()) {
            String tmp = matcher.group();
            uri = uri.replaceAll(tmp, java.net.URLEncoder.encode(tmp, "utf-8"));
        }
        getUrl = uri;
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

    /**
     * GET请求
     * 品牌查询
     *
     * @param token     token(⻋300提供给客户的调⽤帐号,最长32位)
     * @param brandName brandName(品牌名)
     * @param url       url （接口地址）
     *                  返回 json对象 {"update_time":"2016-01-27 14:28:31","initial":"B","brand_name":"宝马","brand_id":"7"}
     *                  brand_id 品牌id
     *                  brand_name 品牌名称
     *                  initial 品牌名称拼音首字母
     *                  update_time 更新时间
     */

    public JSONObject getCarBrandList(String token, String brandName, String url) throws UnsupportedEncodingException {

        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?token=" + token);
        urlParam.append("&brandName=" + brandName);

        //处理url中中文编码
        Matcher matcher = Pattern.compile("[\u4e00-\u9fa5]").matcher(urlParam);
        String getUrl = null;
        String uri = urlParam.toString();
        while (matcher.find()) {
            String tmp = matcher.group();
            uri = uri.replaceAll(tmp, java.net.URLEncoder.encode(tmp, "utf-8"));
        }
        getUrl = uri;
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(getUrl);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");
        JSONObject obj = new JSONObject();
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
            Map maps = JSON.parseObject(result);
            for (Object map : maps.entrySet()) {
                if (((Map.Entry) map).getKey().equals("brand_list")) {
                    JSONArray jsonArray = (JSONArray) ((Map.Entry) map).getValue();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.get("brand_name").equals(brandName)) {
                            obj = jsonObject;
                        }
                    }
                }
            }


            getMethod.releaseConnection();
        } catch (IOException e) {
            log.error("GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}", url, getUrl, e.getMessage(), e);
        }
        return obj;
    }


    /**
     * GET请求
     * 车系查询
     *
     * @param token   token(⻋300提供给客户的调⽤帐号,最长32位)
     * @param brandId brandId(品牌id)
     * @param url     url （接口地址）
     *                series_id 车系 id
     *                series_name 车系名称
     *                maker_type 生产商类型
     *                series_group_name 车系组名
     *                status           状态码 1:表示成功 0:表示失败
     */

    public Map getCarSeriesList(String token, String brandId, String url) throws UnsupportedEncodingException {

        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?token=" + token);
        urlParam.append("&brandId=" + brandId);

        //处理url中中文编码
        Matcher matcher = Pattern.compile("[\u4e00-\u9fa5]").matcher(urlParam);
        String getUrl = null;
        String uri = urlParam.toString();
        while (matcher.find()) {
            String tmp = matcher.group();
            uri = uri.replaceAll(tmp, java.net.URLEncoder.encode(tmp, "utf-8"));
        }
        getUrl = uri;
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(getUrl);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");
        Map maps = new HashMap();
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
            maps = JSON.parseObject(result);
            getMethod.releaseConnection();
        } catch (IOException e) {
            log.error("GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}", url, getUrl, e.getMessage(), e);
        }
        return maps;
    }

    /**
     * GET请求
     * 车型查询
     *
     * @param token    token(⻋300提供给客户的调⽤帐号,最长32位)
     * @param seriesId seriesId(车系标识)
     * @param url      url （接口地址）
     *                 status 是 string 状态码 1:表示成功 0:表示失败
     *                 model_id 是 string，最长 11 车型 ID(整数类型)
     *                 model_name 是 string，最长 100 车型名称
     *                 short_name 是 string，最长 100 车型
     *                 model_price 是 string，最长 4 车型指导价(小数类型)
     *                 model_year 是 string，最长 4 年款(整数类型)
     *                 min_reg_year 是 string，最长 4 最小上牌年份(整数类型)
     *                 max_reg_year 是 string，最长 8 最大上牌年份(整数类型)
     *                 liter 是 string，2 排量
     *                 gear_type 是 string，最长 20 变速箱类型
     *                 discharge_standard 是 string，最长 11 排放标准
     *                 seat_number 是 string，最长 50 座位数
     *                 update_time 是 string，最长 255 更新时间(yyyy-MM-dd HH:mm:ss)
     *                 url 是 string，最长 255
     */

    public Map getCarModelList(String token, String seriesId, String url) throws UnsupportedEncodingException {

        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?token=" + token);
        urlParam.append("&seriesId=" + seriesId);

        //处理url中中文编码
        Matcher matcher = Pattern.compile("[\u4e00-\u9fa5]").matcher(urlParam);
        String getUrl = null;
        String uri = urlParam.toString();
        while (matcher.find()) {
            String tmp = matcher.group();
            uri = uri.replaceAll(tmp, java.net.URLEncoder.encode(tmp, "utf-8"));
        }
        getUrl = uri;
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(getUrl);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");
        Map maps = new HashMap();
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
            maps = JSON.parseObject(result);
            getMethod.releaseConnection();
        } catch (IOException e) {
            log.error("GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}", url, getUrl, e.getMessage(), e);
        }
        return maps;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String url1 = "http://testapi.che300.com/service/getUsedCarPrice";
        String url2 = "http://testapi.che300.com/service/pv/exportModel";
        String url3 = "http://testapi.che300.com/service/getCarBrandList";
        String url4 = "http://testapi.che300.com/service/getCarSeriesList";
        String url5 = "http://testapi.che300.com/service/getCarModelList";

        String token = "61f499b086392005f92009b91f8f966a";
        String modelId = "1128206";
        String carNo = "川G2GG63";
        String mile = "4.0";
        String regDate = "2023-03-01";
        String allLevel = "1";
        String coefficients = "0.1";
        String apiVersion = "v2";
        String fromVersion = "3.5.6201";
        String simple = "0";
        UctpCarInfoSearchUtils uctpCarInfoSearchUtils = new UctpCarInfoSearchUtils();
//        uctpCarInfoSearchUtils.CarMotorcycleType("3.4.561", "0");
//        HashMap map = uctpCarInfoSearchUtils.CarFairValue(modelId, carNo, mile, regDate, allLevel, token, url1, coefficients);
//        uctpCarInfoSearchUtils.CarMotorcycleType(url2, token, apiVersion, fromVersion, simple);
        JSONObject obj = uctpCarInfoSearchUtils.getCarBrandList(token, "宝马", url3);
        Map carSeriesList = uctpCarInfoSearchUtils.getCarSeriesList(token, "5", url4);
        Map carSeriesList1 = uctpCarInfoSearchUtils.getCarModelList(token, "51618", url5);
        System.out.println(carSeriesList1.toString());
//        System.out.println(map.toString());
    }
}
