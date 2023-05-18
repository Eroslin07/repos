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
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UctpCarInfoSearchUtils {
    static Log log = new Log4j2Log(UctpCarInfoSearchUtils.class);

    /**
     * GET请求
     *
     * @param modelId      modelId (⻋型ID) 必传
     * @param carNo        carNo(⻋牌号)  必传
     * @param mile         mile(⻋辆⾏驶⾥程，单位是万公⾥) 必传
     * @param regDate      regDate(⻋辆上牌年-⽉或年-⽉-⽇) 必传
     * @param url          url （接口地址） 必传
     * @param coefficients coefficients车价浮动系数 默认0.2
     */
    public static HashMap CarFairValue(String modelId, String carNo, String mile, String regDate, String url, String coefficients) throws Exception {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("modelId", modelId);
        paramMap.put("carNo", carNo);
        paramMap.put("mile", mile);
        paramMap.put("regDate", regDate);
        paramMap.put("allLevel", "1");
        Map signMap = getSign(paramMap);
        String sign = "";
        String channel = "";
        if (!signMap.isEmpty()) {
            sign = signMap.get("sign").toString();
            channel = signMap.get("channel").toString();
        }

        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?sign=" + sign);
        urlParam.append("&modelId=" + modelId);
        urlParam.append("&carNo=" + carNo);
        urlParam.append("&mile=" + mile);
        urlParam.append("&regDate=" + regDate);
        urlParam.append("&channel=" + channel);
        urlParam.append("&allLevel=" + "1");
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
        getMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
        HashMap mapinfo = new HashMap<>();

        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
//            String result = "{\"resultCode\":\"0000\",\"resultMsg\":\"获取估值成功\",\"resultData\":{\"default_car_condition\":\"good\",\"model_price\":\"8.99\",\"eval_prices\":[{\"condition\":\"excellent\",\"individual_price\":3.47,\"individual_low_sold_price\":3.36,\"dealer_low_buy_price\":3.13,\"dealer_high_sold_price\":3.81,\"dealer_price\":3.63,\"dealer_buy_price\":3.23,\"dealer_low_sold_price\":3.51},{\"condition\":\"good\",\"individual_price\":3.25,\"individual_low_sold_price\":3.15,\"dealer_low_buy_price\":2.93,\"dealer_high_sold_price\":3.57,\"dealer_price\":3.4,\"dealer_buy_price\":3.03,\"dealer_low_sold_price\":3.29},{\"condition\":\"normal\",\"individual_price\":3.03,\"individual_low_sold_price\":2.94,\"dealer_low_buy_price\":2.74,\"dealer_high_sold_price\":3.33,\"dealer_price\":3.18,\"dealer_buy_price\":2.83,\"dealer_low_sold_price\":3.07}],\"detail_report_url\":\"https://www.ceshi.che300.com/pinggu/v22c244m25497r2016-08g12?from=YHCS&_s=1d29f3964bc56bd7\",\"discharge_standard\":\"国4\",\"status\":\"1\"}}";
            System.out.println("获取车辆价值————————" + result);
            ObjectMapper mapper = new ObjectMapper();
            HashMap responseJson = mapper.readValue(result, HashMap.class);
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
            // dealer_low_buy_price 最低⻋商收购价
            String dealer_low_buy_price = "";
            // dealer_buy_price ⻋商收⻋价
            String dealer_buy_price = "";
            // Recommended_low_buy_price 推荐最低车商收车价
            String Recommended_low_buy_price = null;
            // Recommended_buy_price 推荐车商收车价
            String Recommended_buy_price = null;

            //转换系数类型  默认为0.2,
            Double coefficient = new Double(StringUtil.isEmpty(coefficients) ? "0.2" : coefficients);
            String resultCode = JsonPath.read(responseJson, "$.resultCode");
            if (!StringUtil.isEmpty(resultCode) && resultCode.equals("0000")) {
                List<Object> eval_prices = JsonPath.read(responseJson, "$..eval_prices");
                List<Object> model_priceL = JsonPath.read(responseJson, "$..model_price");
                model_price = model_priceL.get(0).toString();
                JSONArray eval_pricesArr = JSONArray.parseArray(eval_prices.toString());
                for (int i = 0; i < eval_pricesArr.size(); i++) {
                    JSONArray o = (JSONArray) eval_pricesArr.get(i);
                    for (int j = 0; j < o.size(); j++) {
                        JSONObject o1 = (JSONObject) o.get(j);
                        HashMap<String, String> map = new HashMap();
                        for (Map.Entry entry : o1.entrySet()) {
                            map.put(entry.getKey().toString(), entry.getValue().toString());
                        }
                        if (!StringUtil.isEmpty(map.get("condition")) && map.get("condition").equals("normal")) {
                            dealer_low_sold_price = map.get("dealer_low_sold_price");
                            System.out.println("dealer_low_sold_price" + dealer_low_sold_price);
                            dealer_low_buy_price = map.get("dealer_low_buy_price");
                            System.out.println("dealer_low_buy_price" + dealer_low_buy_price);
                        }
                        if (!StringUtil.isEmpty(map.get("condition")) && map.get("condition").equals("excellent")) {
                            dealer_high_sold_price = map.get("dealer_high_sold_price");
                            System.out.println("dealer_high_sold_price" + dealer_high_sold_price);
                            dealer_buy_price = map.get("dealer_buy_price");
                            System.out.println("dealer_buy_price" + dealer_buy_price);
                        }
                    }
                }
                //  add():加法， subtract():减法:, multiply():乘法; divide():除法，
                Recommended_low_sold_price = String.format("%.2f", new BigDecimal(dealer_low_sold_price).multiply(new BigDecimal(1 - coefficient)));
                Recommended_high_sold_price = String.format("%.2f", new BigDecimal(dealer_high_sold_price).multiply(new BigDecimal(1 + coefficient)));
                Recommended_low_buy_price = String.format("%.2f", new BigDecimal(dealer_low_buy_price).multiply(new BigDecimal(1 + coefficient)));
                Recommended_buy_price = String.format("%.2f", new BigDecimal(dealer_buy_price).multiply(new BigDecimal(1 + coefficient)));

                mapinfo.put("dealer_low_sold_price", dealer_low_sold_price);
                mapinfo.put("dealer_high_sold_price", dealer_high_sold_price);
                mapinfo.put("Recommended_low_sold_price", Recommended_low_sold_price);
                mapinfo.put("Recommended_high_sold_price", Recommended_high_sold_price);
                mapinfo.put("Recommended_low_buy_price", Recommended_low_buy_price);
                mapinfo.put("Recommended_buy_price", Recommended_buy_price);
                mapinfo.put("model_price", model_price);
                mapinfo.put("msg", "success");

                getMethod.releaseConnection();

            } else {
                mapinfo.put("msg", "估值接口异常");
            }

            return mapinfo;

        } catch (
                IOException e) {
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

    public static JSONArray getCarBrandList(String url) throws Exception {
        Map<String, Object> paramMap = new HashMap();
        Map signMap = getSign(paramMap);
        String sign = "";
        String channel = "";
        if (!signMap.isEmpty()) {
            sign = signMap.get("sign").toString();
            channel = signMap.get("channel").toString();
        }
        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?sign=" + sign);
        urlParam.append("&channel=" + channel);

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
        JSONArray array = new JSONArray();
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
//            System.out.println("获取车辆品牌信息————————" + result);
            Map maps = JSON.parseObject(result);
            for (Object map : maps.entrySet()) {
                if (((Map.Entry) map).getKey().equals("resultData")) {
                    array = (JSONArray) ((Map.Entry) map).getValue();

                }
            }


            getMethod.releaseConnection();
        } catch (IOException e) {
            log.error("GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}", url, getUrl, e.getMessage(), e);
        }
        return array;
    }


    /**
     * GET请求
     * 品牌查询
     *
     * @param
     * @param brandName brandName(品牌名)
     * @param url       url （接口地址）
     *                  返回 json对象 {"update_time":"2016-01-27 14:28:31","initial":"B","brand_name":"宝马","brand_id":"7"}
     *                  brand_id 品牌id
     *                  brand_name 品牌名称
     *                  initial 品牌名称拼音首字母
     *                  update_time 更新时间
     */

    public static JSONObject getCarBrandList(String brandName, String url) throws Exception {
        Map<String, Object> paramMap = new HashMap();
//        paramMap.put("brandName", brandName);
        Map signMap = getSign(paramMap);
        String sign = "";
        String channel = "";
        if (!signMap.isEmpty()) {
            sign = signMap.get("sign").toString();
            channel = signMap.get("channel").toString();
        }
        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?sign=" + sign);
        urlParam.append("&channel=" + channel);

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
//            String result = "{\"resultCode\":\"0000\",\"resultMsg\":\"获取品牌列表成功\",\"resultData\":[{\"update_time\":\"2022-01-18 18:28:07\",\"initial\":\"A\",\"brand_name\":\"奥迪\",\"brand_id\":\"1\"},{\"update_time\":\"2016-01-27 13:41:17\",\"initial\":\"A\",\"brand_name\":\"阿尔法·罗密欧\",\"brand_id\":\"3\"},{\"update_time\":\"2016-01-27 13:17:18\",\"initial\":\"A\",\"brand_name\":\"阿斯顿·马丁\",\"brand_id\":\"2\"},{\"update_time\":\"2022-08-09 18:22:36\",\"initial\":\"A\",\"brand_name\":\"阿维塔\",\"brand_id\":\"2931\"},{\"update_time\":\"2021-04-01 17:20:02\",\"initial\":\"A\",\"brand_name\":\"ARCFOX极狐\",\"brand_id\":\"1721\"},{\"update_time\":\"2021-12-24 14:44:29\",\"initial\":\"A\",\"brand_name\":\"AITO\",\"brand_id\":\"2440\"},{\"update_time\":\"2019-06-28 18:26:13\",\"initial\":\"A\",\"brand_name\":\"AC Schnitzer\",\"brand_id\":\"4\"},{\"update_time\":\"2017-09-25 14:41:19\",\"initial\":\"A\",\"brand_name\":\"ALPINA\",\"brand_id\":\"536\"},{\"update_time\":\"2020-06-16 13:36:40\",\"initial\":\"A\",\"brand_name\":\"AUXUN傲旋\",\"brand_id\":\"1714\"},{\"update_time\":\"2021-01-05 22:20:43\",\"initial\":\"A\",\"brand_name\":\"安凯客车\",\"brand_id\":\"610\"},{\"update_time\":\"2019-12-25 17:41:21\",\"initial\":\"A\",\"brand_name\":\"爱驰\",\"brand_id\":\"1407\"},{\"update_time\":\"2016-01-27 14:28:31\",\"initial\":\"B\",\"brand_name\":\"宝马\",\"brand_id\":\"7\"},{\"update_time\":\"2016-01-27 14:28:10\",\"initial\":\"B\",\"brand_name\":\"宝骏\",\"brand_id\":\"15\"},{\"update_time\":\"2016-06-14 14:36:53\",\"initial\":\"B\",\"brand_name\":\"宝沃\",\"brand_id\":\"172\"},{\"update_time\":\"2016-01-26 22:54:37\",\"initial\":\"B\",\"brand_name\":\"宝龙\",\"brand_id\":\"144\"},{\"update_time\":\"2016-01-27 14:28:30\",\"initial\":\"B\",\"brand_name\":\"本田\",\"brand_id\":\"5\"},{\"update_time\":\"2018-06-19 18:33:01\",\"initial\":\"B\",\"brand_name\":\"奔驰\",\"brand_id\":\"9\"},{\"update_time\":\"2016-01-27 14:24:59\",\"initial\":\"B\",\"brand_name\":\"奔腾\",\"brand_id\":\"12\"},{\"update_time\":\"2016-01-27 14:28:47\",\"initial\":\"B\",\"brand_name\":\"别克\",\"brand_id\":\"6\"},{\"update_time\":\"2016-01-27 14:25:47\",\"initial\":\"B\",\"brand_name\":\"比亚迪\",\"brand_id\":\"8\"},{\"update_time\":\"2017-01-03 16:09:23\",\"initial\":\"B\",\"brand_name\":\"比速\",\"brand_id\":\"499\"},{\"update_time\":\"2019-09-03 19:04:06\",\"initial\":\"B\",\"brand_name\":\"比德文汽车\",\"brand_id\":\"853\"},{\"update_time\":\"2018-04-03 11:20:37\",\"initial\":\"B\",\"brand_name\":\"保时捷\",\"brand_id\":\"11\"},{\"update_time\":\"2016-01-27 13:27:48\",\"initial\":\"B\",\"brand_name\":\"保斐利\",\"brand_id\":\"20\"},{\"update_time\":\"2016-01-27 14:27:48\",\"initial\":\"B\",\"brand_name\":\"标致\",\"brand_id\":\"10\"},{\"update_time\":\"2016-01-27 14:05:36\",\"initial\":\"B\",\"brand_name\":\"北京汽车\",\"brand_id\":\"13\"},{\"update_time\":\"2019-07-24 16:45:11\",\"initial\":\"B\",\"brand_name\":\"北京清行\",\"brand_id\":\"837\"},{\"update_time\":\"2016-01-27 14:14:50\",\"initial\":\"B\",\"brand_name\":\"北汽幻速\",\"brand_id\":\"156\"},{\"update_time\":\"2016-02-16 14:30:54\",\"initial\":\"B\",\"brand_name\":\"北汽新能源\",\"brand_id\":\"167\"},{\"update_time\":\"2016-01-27 11:08:25\",\"initial\":\"B\",\"brand_name\":\"北汽威旺\",\"brand_id\":\"17\"},{\"update_time\":\"2016-01-27 14:24:03\",\"initial\":\"B\",\"brand_name\":\"北汽制造\",\"brand_id\":\"14\"},{\"update_time\":\"2021-08-31 17:35:23\",\"initial\":\"B\",\"brand_name\":\"北汽瑞翔\",\"brand_id\":\"2398\"},{\"update_time\":\"2018-02-24 10:04:49\",\"initial\":\"B\",\"brand_name\":\"北汽道达\",\"brand_id\":\"573\"},{\"update_time\":\"2021-01-11 15:25:20\",\"initial\":\"B\",\"brand_name\":\"BEIJING汽车\",\"brand_id\":\"115\"},{\"update_time\":\"2016-01-27 12:35:16\",\"initial\":\"B\",\"brand_name\":\"宾利\",\"brand_id\":\"16\"},{\"update_time\":\"2016-01-26 17:59:49\",\"initial\":\"B\",\"brand_name\":\"巴博斯\",\"brand_id\":\"19\"},{\"update_time\":\"2021-10-20 15:22:11\",\"initial\":\"B\",\"brand_name\":\"铂驰\",\"brand_id\":\"654\"},{\"update_time\":\"2016-01-27 14:10:18\",\"initial\":\"B\",\"brand_name\":\"布加迪\",\"brand_id\":\"18\"},{\"update_time\":\"2021-12-02 14:44:32\",\"initial\":\"B\",\"brand_name\":\"百智新能源\",\"brand_id\":\"2432\"},{\"update_time\":\"2018-10-19 09:44:34\",\"initial\":\"C\",\"brand_name\":\"长安\",\"brand_id\":\"21\"},{\"update_time\":\"2018-09-20 21:55:05\",\"initial\":\"C\",\"brand_name\":\"长安欧尚\",\"brand_id\":\"23\"},{\"update_time\":\"2022-08-08 09:47:56\",\"initial\":\"C\",\"brand_name\":\"长安深蓝\",\"brand_id\":\"2924\"},{\"update_time\":\"2016-01-27 14:20:36\",\"initial\":\"C\",\"brand_name\":\"长城\",\"brand_id\":\"22\"},{\"update_time\":\"2018-04-08 17:04:20\",\"initial\":\"C\",\"brand_name\":\"昌河\",\"brand_id\":\"24\"},{\"update_time\":\"2021-07-06 14:11:28\",\"initial\":\"C\",\"brand_name\":\"创维汽车\",\"brand_id\":\"1768\"},{\"update_time\":\"2017-01-03 16:09:18\",\"initial\":\"C\",\"brand_name\":\"成功\",\"brand_id\":\"497\"},{\"update_time\":\"2023-01-03 15:59:39\",\"initial\":\"C\",\"brand_name\":\"测试品牌 2313\",\"brand_id\":\"29831\"},{\"update_time\":\"2022-11-23 10:01:27\",\"initial\":\"C\",\"brand_name\":\"橙仕 1229\",\"brand_id\":\"2983\"},{\"update_time\":\"2020-05-08 18:53:25\",\"initial\":\"C\",\"brand_name\":\"车驰汽车\",\"brand_id\":\"1716\"},{\"update_time\":\"2016-01-27 14:28:54\",\"initial\":\"D\",\"brand_name\":\"大众\",\"brand_id\":\"25\"},{\"update_time\":\"2018-09-29 10:04:36\",\"initial\":\"D\",\"brand_name\":\"大乘汽车\",\"brand_id\":\"639\"},{\"update_time\":\"2020-05-05 23:31:30\",\"initial\":\"D\",\"brand_name\":\"大运\",\"brand_id\":\"1705\"},{\"update_time\":\"2017-01-03 16:12:04\",\"initial\":\"D\",\"brand_name\":\"大迪\",\"brand_id\":\"142\"},{\"update_time\":\"2016-01-27 14:26:45\",\"initial\":\"D\",\"brand_name\":\"东风风行\",\"brand_id\":\"26\"},{\"update_time\":\"2016-03-18 17:22:19\",\"initial\":\"D\",\"brand_name\":\"东风风光\",\"brand_id\":\"170\"},{\"update_time\":\"2016-01-27 14:27:48\",\"initial\":\"D\",\"brand_name\":\"东风风神\",\"brand_id\":\"30\"},{\"update_time\":\"2022-04-08 17:36:16\",\"initial\":\"D\",\"brand_name\":\"东风\",\"brand_id\":\"33\"},{\"update_time\":\"2016-01-27 13:55:02\",\"initial\":\"D\",\"brand_name\":\"东风小康\",\"brand_id\":\"28\"},{\"update_time\":\"2016-01-27 13:08:23\",\"initial\":\"D\",\"brand_name\":\"东风风度\",\"brand_id\":\"32\"},{\"update_time\":\"2020-05-28 11:56:26\",\"initial\":\"D\",\"brand_name\":\"东风瑞泰特\",\"brand_id\":\"620\"},{\"update_time\":\"2016-01-27 13:49:50\",\"initial\":\"D\",\"brand_name\":\"东南\",\"brand_id\":\"27\"},{\"update_time\":\"2016-01-27 13:03:59\",\"initial\":\"D\",\"brand_name\":\"道奇\",\"brand_id\":\"29\"},{\"update_time\":\"2016-01-27 13:42:40\",\"initial\":\"D\",\"brand_name\":\"DS\",\"brand_id\":\"31\"},{\"update_time\":\"2021-09-01 18:06:36\",\"initial\":\"D\",\"brand_name\":\"电动屋\",\"brand_id\":\"2399\"},{\"update_time\":\"2018-02-24 10:25:51\",\"initial\":\"D\",\"brand_name\":\"电咖\",\"brand_id\":\"574\"},{\"update_time\":\"2021-01-05 22:20:46\",\"initial\":\"D\",\"brand_name\":\"达契亚\",\"brand_id\":\"662\"},{\"update_time\":\"2016-01-27 14:28:27\",\"initial\":\"F\",\"brand_name\":\"丰田\",\"brand_id\":\"36\"},{\"update_time\":\"2016-01-27 14:27:49\",\"initial\":\"F\",\"brand_name\":\"福特\",\"brand_id\":\"35\"},{\"update_time\":\"2016-01-27 14:28:52\",\"initial\":\"F\",\"brand_name\":\"福田\",\"brand_id\":\"39\"},{\"update_time\":\"2017-07-24 10:38:27\",\"initial\":\"F\",\"brand_name\":\"福田乘用车\",\"brand_id\":\"545\"},{\"update_time\":\"2016-01-27 11:13:04\",\"initial\":\"F\",\"brand_name\":\"福迪\",\"brand_id\":\"40\"},{\"update_time\":\"2016-01-27 00:34:12\",\"initial\":\"F\",\"brand_name\":\"福汽启腾\",\"brand_id\":\"162\"},{\"update_time\":\"2016-01-27 14:27:05\",\"initial\":\"F\",\"brand_name\":\"菲亚特\",\"brand_id\":\"37\"},{\"update_time\":\"2018-05-10 14:27:53\",\"initial\":\"F\",\"brand_name\":\"菲斯克\",\"brand_id\":\"42\"},{\"update_time\":\"2022-07-26 09:46:56\",\"initial\":\"F\",\"brand_name\":\"飞凡汽车\",\"brand_id\":\"2314\"},{\"update_time\":\"2017-06-19 20:13:09\",\"initial\":\"F\",\"brand_name\":\"飞碟\",\"brand_id\":\"543\"},{\"update_time\":\"2016-01-27 11:13:05\",\"initial\":\"F\",\"brand_name\":\"飞驰商务车\",\"brand_id\":\"41\"},{\"update_time\":\"2016-01-27 13:55:58\",\"initial\":\"F\",\"brand_name\":\"法拉利\",\"brand_id\":\"38\"},{\"update_time\":\"2020-05-05 18:22:50\",\"initial\":\"F\",\"brand_name\":\"枫叶汽车\",\"brand_id\":\"1704\"},{\"update_time\":\"2019-10-12 16:49:43\",\"initial\":\"F\",\"brand_name\":\"富奇\",\"brand_id\":\"147\"},{\"update_time\":\"2016-03-22 14:12:00\",\"initial\":\"G\",\"brand_name\":\"广汽传祺\",\"brand_id\":\"44\"},{\"update_time\":\"2022-06-28 09:40:23\",\"initial\":\"G\",\"brand_name\":\"广汽埃安\",\"brand_id\":\"2686\"},{\"update_time\":\"2018-09-12 18:13:39\",\"initial\":\"G\",\"brand_name\":\"广汽新能源\",\"brand_id\":\"636\"},{\"update_time\":\"2016-01-27 14:16:23\",\"initial\":\"G\",\"brand_name\":\"广汽吉奥\",\"brand_id\":\"45\"},{\"update_time\":\"2016-03-22 14:14:55\",\"initial\":\"G\",\"brand_name\":\"观致\",\"brand_id\":\"46\"},{\"update_time\":\"2016-01-27 11:58:09\",\"initial\":\"G\",\"brand_name\":\"GMC\",\"brand_id\":\"47\"},{\"update_time\":\"2016-01-27 11:14:09\",\"initial\":\"G\",\"brand_name\":\"光冈\",\"brand_id\":\"48\"},{\"update_time\":\"2020-09-25 09:24:15\",\"initial\":\"G\",\"brand_name\":\"高合汽车\",\"brand_id\":\"1772\"},{\"update_time\":\"2019-11-01 17:51:28\",\"initial\":\"G\",\"brand_name\":\"国机智骏\",\"brand_id\":\"1075\"},{\"update_time\":\"2018-02-24 10:41:15\",\"initial\":\"G\",\"brand_name\":\"国金\",\"brand_id\":\"586\"},{\"update_time\":\"2016-01-27 14:28:44\",\"initial\":\"H\",\"brand_name\":\"哈弗\",\"brand_id\":\"50\"},{\"update_time\":\"2016-01-27 13:58:39\",\"initial\":\"H\",\"brand_name\":\"哈飞\",\"brand_id\":\"56\"},{\"update_time\":\"2016-01-27 11:59:19\",\"initial\":\"H\",\"brand_name\":\"红旗\",\"brand_id\":\"53\"},{\"update_time\":\"2018-07-05 09:35:58\",\"initial\":\"H\",\"brand_name\":\"红星汽车\",\"brand_id\":\"618\"},{\"update_time\":\"2016-01-27 14:28:09\",\"initial\":\"H\",\"brand_name\":\"海马\",\"brand_id\":\"51\"},{\"update_time\":\"2016-01-27 11:15:35\",\"initial\":\"H\",\"brand_name\":\"海马商用车\",\"brand_id\":\"54\"},{\"update_time\":\"2016-01-27 11:15:41\",\"initial\":\"H\",\"brand_name\":\"海格\",\"brand_id\":\"57\"},{\"update_time\":\"2017-01-03 16:09:16\",\"initial\":\"H\",\"brand_name\":\"汉腾\",\"brand_id\":\"495\"},{\"update_time\":\"2019-12-13 14:35:16\",\"initial\":\"H\",\"brand_name\":\"汉龙汽车\",\"brand_id\":\"1300\"},{\"update_time\":\"2016-01-27 11:38:56\",\"initial\":\"H\",\"brand_name\":\"华泰\",\"brand_id\":\"52\"},{\"update_time\":\"2016-07-08 22:22:55\",\"initial\":\"H\",\"brand_name\":\"华泰新能源\",\"brand_id\":\"173\"},{\"update_time\":\"2016-01-27 07:08:55\",\"initial\":\"H\",\"brand_name\":\"华颂\",\"brand_id\":\"160\"},{\"update_time\":\"2021-06-30 18:30:19\",\"initial\":\"H\",\"brand_name\":\"华晨新日\",\"brand_id\":\"2385\"},{\"update_time\":\"2016-01-27 14:19:51\",\"initial\":\"H\",\"brand_name\":\"华普\",\"brand_id\":\"146\"},{\"update_time\":\"2022-08-26 13:44:17\",\"initial\":\"H\",\"brand_name\":\"华梓汽车\",\"brand_id\":\"2937\"},{\"update_time\":\"2017-12-07 13:46:53\",\"initial\":\"H\",\"brand_name\":\"华骐\",\"brand_id\":\"560\"},{\"update_time\":\"2016-01-27 13:25:29\",\"initial\":\"H\",\"brand_name\":\"黄海\",\"brand_id\":\"55\"},{\"update_time\":\"2016-01-27 12:56:34\",\"initial\":\"H\",\"brand_name\":\"悍马\",\"brand_id\":\"145\"},{\"update_time\":\"2016-01-27 11:15:43\",\"initial\":\"H\",\"brand_name\":\"汇众\",\"brand_id\":\"59\"},{\"update_time\":\"2020-10-10 18:06:38\",\"initial\":\"H\",\"brand_name\":\"航天凌河\",\"brand_id\":\"2094\"},{\"update_time\":\"2020-04-16 10:12:37\",\"initial\":\"H\",\"brand_name\":\"HYCAN合创\",\"brand_id\":\"1703\"},{\"update_time\":\"2021-04-07 19:01:49\",\"initial\":\"H\",\"brand_name\":\"宏瑞汽车\",\"brand_id\":\"2363\"},{\"update_time\":\"2017-01-03 16:13:37\",\"initial\":\"H\",\"brand_name\":\"恒天\",\"brand_id\":\"58\"},{\"update_time\":\"2021-06-30 18:40:56\",\"initial\":\"H\",\"brand_name\":\"恒润汽车\",\"brand_id\":\"2386\"},{\"update_time\":\"2022-07-22 23:10:38\",\"initial\":\"H\",\"brand_name\":\"恒驰\",\"brand_id\":\"2918\"},{\"update_time\":\"2021-01-05 22:20:46\",\"initial\":\"H\",\"brand_name\":\"霍顿\",\"brand_id\":\"668\"},{\"update_time\":\"2021-01-14 18:28:38\",\"initial\":\"H\",\"brand_name\":\"黑豹\",\"brand_id\":\"2279\"},{\"update_time\":\"2020-08-19 15:56:04\",\"initial\":\"I\",\"brand_name\":\"IMSA英飒\",\"brand_id\":\"1766\"},{\"update_time\":\"2016-01-27 14:05:55\",\"initial\":\"J\",\"brand_name\":\"吉利\",\"brand_id\":\"143\"},{\"update_time\":\"2016-01-27 14:27:09\",\"initial\":\"J\",\"brand_name\":\"吉利帝豪\",\"brand_id\":\"63\"},{\"update_time\":\"2016-01-27 14:12:56\",\"initial\":\"J\",\"brand_name\":\"吉利全球鹰\",\"brand_id\":\"62\"},{\"update_time\":\"2016-01-27 14:22:26\",\"initial\":\"J\",\"brand_name\":\"吉利英伦\",\"brand_id\":\"65\"},{\"update_time\":\"2016-01-27 14:23:55\",\"initial\":\"J\",\"brand_name\":\"江淮\",\"brand_id\":\"60\"},{\"update_time\":\"2016-01-27 14:11:19\",\"initial\":\"J\",\"brand_name\":\"江铃\",\"brand_id\":\"66\"},{\"update_time\":\"2022-07-16 17:40:15\",\"initial\":\"J\",\"brand_name\":\"江铃集团新能源\",\"brand_id\":\"542\"},{\"update_time\":\"2016-01-27 11:22:46\",\"initial\":\"J\",\"brand_name\":\"江南\",\"brand_id\":\"68\"},{\"update_time\":\"2016-01-27 14:22:38\",\"initial\":\"J\",\"brand_name\":\"捷豹\",\"brand_id\":\"64\"},{\"update_time\":\"2018-08-20 13:50:56\",\"initial\":\"J\",\"brand_name\":\"捷途汽车\",\"brand_id\":\"634\"},{\"update_time\":\"2019-09-05 18:16:05\",\"initial\":\"J\",\"brand_name\":\"捷达\",\"brand_id\":\"852\"},{\"update_time\":\"2021-04-23 18:21:26\",\"initial\":\"J\",\"brand_name\":\"捷尼赛思\",\"brand_id\":\"2367\"},{\"update_time\":\"2016-01-27 14:18:41\",\"initial\":\"J\",\"brand_name\":\"Jeep\",\"brand_id\":\"61\"},{\"update_time\":\"2016-01-27 14:13:10\",\"initial\":\"J\",\"brand_name\":\"金杯\",\"brand_id\":\"67\"},{\"update_time\":\"2016-01-27 11:22:47\",\"initial\":\"J\",\"brand_name\":\"金龙联合\",\"brand_id\":\"69\"},{\"update_time\":\"2016-01-27 11:22:48\",\"initial\":\"J\",\"brand_name\":\"金旅客车\",\"brand_id\":\"71\"},{\"update_time\":\"2020-04-28 13:25:12\",\"initial\":\"J\",\"brand_name\":\"金冠\",\"brand_id\":\"657\"},{\"update_time\":\"2019-05-31 15:22:13\",\"initial\":\"J\",\"brand_name\":\"几何汽车\",\"brand_id\":\"825\"},{\"update_time\":\"2021-04-16 18:39:20\",\"initial\":\"J\",\"brand_name\":\"极氪\",\"brand_id\":\"2366\"},{\"update_time\":\"2018-02-24 09:40:06\",\"initial\":\"J\",\"brand_name\":\"君马汽车\",\"brand_id\":\"572\"},{\"update_time\":\"2016-01-27 11:22:47\",\"initial\":\"J\",\"brand_name\":\"九龙\",\"brand_id\":\"70\"},{\"update_time\":\"2019-04-18 17:39:36\",\"initial\":\"J\",\"brand_name\":\"巨威\",\"brand_id\":\"817\"},{\"update_time\":\"2019-03-28 21:47:15\",\"initial\":\"J\",\"brand_name\":\"钧天\",\"brand_id\":\"752\"},{\"update_time\":\"2016-01-27 13:17:57\",\"initial\":\"K\",\"brand_name\":\"凯迪拉克\",\"brand_id\":\"73\"},{\"update_time\":\"2016-01-27 00:30:44\",\"initial\":\"K\",\"brand_name\":\"凯翼\",\"brand_id\":\"157\"},{\"update_time\":\"2021-03-30 21:31:54\",\"initial\":\"K\",\"brand_name\":\"凯伦宾威\",\"brand_id\":\"818\"},{\"update_time\":\"2020-08-11 19:00:43\",\"initial\":\"K\",\"brand_name\":\"凯佰赫\",\"brand_id\":\"678\"},{\"update_time\":\"2019-05-16 11:15:51\",\"initial\":\"K\",\"brand_name\":\"凯马\",\"brand_id\":\"819\"},{\"update_time\":\"2016-01-27 14:20:04\",\"initial\":\"K\",\"brand_name\":\"克莱斯勒\",\"brand_id\":\"74\"},{\"update_time\":\"2022-02-28 18:40:47\",\"initial\":\"K\",\"brand_name\":\"克蒂汽车\",\"brand_id\":\"2662\"},{\"update_time\":\"2016-01-27 14:25:51\",\"initial\":\"K\",\"brand_name\":\"开瑞\",\"brand_id\":\"75\"},{\"update_time\":\"2016-01-26 01:49:52\",\"initial\":\"K\",\"brand_name\":\"卡威\",\"brand_id\":\"158\"},{\"update_time\":\"2017-08-04 09:44:28\",\"initial\":\"K\",\"brand_name\":\"康迪全球鹰\",\"brand_id\":\"546\"},{\"update_time\":\"2018-01-04 17:33:28\",\"initial\":\"K\",\"brand_name\":\"卡升\",\"brand_id\":\"562\"},{\"update_time\":\"2016-01-27 11:25:53\",\"initial\":\"K\",\"brand_name\":\"卡尔森\",\"brand_id\":\"77\"},{\"update_time\":\"2020-12-06 09:31:17\",\"initial\":\"K\",\"brand_name\":\"Karma\",\"brand_id\":\"1720\"},{\"update_time\":\"2020-03-24 16:28:39\",\"initial\":\"K\",\"brand_name\":\"KTM\",\"brand_id\":\"552\"},{\"update_time\":\"2018-02-08 16:29:17\",\"initial\":\"K\",\"brand_name\":\"科尼赛克\",\"brand_id\":\"76\"},{\"update_time\":\"2016-01-27 13:53:43\",\"initial\":\"L\",\"brand_name\":\"雷克萨斯\",\"brand_id\":\"80\"},{\"update_time\":\"2016-01-27 14:25:20\",\"initial\":\"L\",\"brand_name\":\"雷诺\",\"brand_id\":\"84\"},{\"update_time\":\"2019-08-22 10:55:47\",\"initial\":\"L\",\"brand_name\":\"雷丁\",\"brand_id\":\"640\"},{\"update_time\":\"2016-01-27 14:23:17\",\"initial\":\"L\",\"brand_name\":\"路虎\",\"brand_id\":\"79\"},{\"update_time\":\"2016-01-27 13:45:38\",\"initial\":\"L\",\"brand_name\":\"路特斯\",\"brand_id\":\"90\"},{\"update_time\":\"2017-12-27 13:36:58\",\"initial\":\"L\",\"brand_name\":\"领克\",\"brand_id\":\"561\"},{\"update_time\":\"2018-11-20 17:31:26\",\"initial\":\"L\",\"brand_name\":\"领途汽车\",\"brand_id\":\"682\"},{\"update_time\":\"2016-01-27 14:25:51\",\"initial\":\"L\",\"brand_name\":\"铃木\",\"brand_id\":\"78\"},{\"update_time\":\"2016-01-27 11:54:56\",\"initial\":\"L\",\"brand_name\":\"林肯\",\"brand_id\":\"87\"},{\"update_time\":\"2017-01-03 16:13:46\",\"initial\":\"L\",\"brand_name\":\"猎豹\",\"brand_id\":\"85\"},{\"update_time\":\"2019-04-15 18:18:59\",\"initial\":\"L\",\"brand_name\":\"理想\",\"brand_id\":\"815\"},{\"update_time\":\"2016-01-27 11:34:36\",\"initial\":\"L\",\"brand_name\":\"理念\",\"brand_id\":\"89\"},{\"update_time\":\"2016-01-27 14:07:50\",\"initial\":\"L\",\"brand_name\":\"陆风\",\"brand_id\":\"83\"},{\"update_time\":\"2018-07-06 17:58:52\",\"initial\":\"L\",\"brand_name\":\"陆地方舟\",\"brand_id\":\"619\"},{\"update_time\":\"2019-01-07 11:47:56\",\"initial\":\"L\",\"brand_name\":\"零跑汽车\",\"brand_id\":\"750\"},{\"update_time\":\"2016-01-27 13:34:35\",\"initial\":\"L\",\"brand_name\":\"力帆\",\"brand_id\":\"81\"},{\"update_time\":\"2016-01-27 14:21:59\",\"initial\":\"L\",\"brand_name\":\"劳斯莱斯\",\"brand_id\":\"86\"},{\"update_time\":\"2016-01-27 12:45:15\",\"initial\":\"L\",\"brand_name\":\"兰博基尼\",\"brand_id\":\"82\"},{\"update_time\":\"2022-08-10 17:35:42\",\"initial\":\"L\",\"brand_name\":\"岚图汽车\",\"brand_id\":\"2365\"},{\"update_time\":\"2016-01-27 12:10:35\",\"initial\":\"L\",\"brand_name\":\"莲花\",\"brand_id\":\"88\"},{\"update_time\":\"2018-10-16 11:11:51\",\"initial\":\"L\",\"brand_name\":\"Lorinser\",\"brand_id\":\"661\"},{\"update_time\":\"2021-09-26 13:49:14\",\"initial\":\"L\",\"brand_name\":\"LEVC\",\"brand_id\":\"2402\"},{\"update_time\":\"2019-09-17 14:00:38\",\"initial\":\"L\",\"brand_name\":\"LITE\",\"brand_id\":\"587\"},{\"update_time\":\"2022-04-13 17:26:32\",\"initial\":\"L\",\"brand_name\":\"LUMMA\",\"brand_id\":\"2672\"},{\"update_time\":\"2020-05-15 15:23:18\",\"initial\":\"L\",\"brand_name\":\"凌宝汽车\",\"brand_id\":\"1718\"},{\"update_time\":\"2018-03-09 16:59:22\",\"initial\":\"L\",\"brand_name\":\"拉达LADA\",\"brand_id\":\"588\"},{\"update_time\":\"2018-10-25 11:13:09\",\"initial\":\"L\",\"brand_name\":\"罗夫哈特\",\"brand_id\":\"712\"},{\"update_time\":\"2016-01-27 14:28:37\",\"initial\":\"M\",\"brand_name\":\"马自达\",\"brand_id\":\"92\"},{\"update_time\":\"2019-11-23 09:56:37\",\"initial\":\"M\",\"brand_name\":\"名爵\",\"brand_id\":\"93\"},{\"update_time\":\"2016-01-27 14:27:28\",\"initial\":\"M\",\"brand_name\":\"MINI\",\"brand_id\":\"94\"},{\"update_time\":\"2016-01-27 14:07:42\",\"initial\":\"M\",\"brand_name\":\"玛莎拉蒂\",\"brand_id\":\"96\"},{\"update_time\":\"2016-01-27 12:50:07\",\"initial\":\"M\",\"brand_name\":\"迈凯伦\",\"brand_id\":\"97\"},{\"update_time\":\"2016-01-27 12:46:54\",\"initial\":\"M\",\"brand_name\":\"迈巴赫\",\"brand_id\":\"95\"},{\"update_time\":\"2019-07-24 17:18:44\",\"initial\":\"M\",\"brand_name\":\"迈莎锐\",\"brand_id\":\"820\"},{\"update_time\":\"2019-12-25 18:53:35\",\"initial\":\"M\",\"brand_name\":\"迈迈\",\"brand_id\":\"1298\"},{\"update_time\":\"2018-12-10 11:12:15\",\"initial\":\"M\",\"brand_name\":\"迈迪汽车\",\"brand_id\":\"641\"},{\"update_time\":\"2021-06-24 18:37:57\",\"initial\":\"M\",\"brand_name\":\"摩登汽车\",\"brand_id\":\"2384\"},{\"update_time\":\"2016-01-27 11:41:25\",\"initial\":\"M\",\"brand_name\":\"摩根\",\"brand_id\":\"98\"},{\"update_time\":\"2017-11-14 12:49:04\",\"initial\":\"M\",\"brand_name\":\"明君汽车\",\"brand_id\":\"558\"},{\"update_time\":\"2016-01-27 11:41:25\",\"initial\":\"M\",\"brand_name\":\"美亚\",\"brand_id\":\"99\"},{\"update_time\":\"2016-01-27 14:20:08\",\"initial\":\"N\",\"brand_name\":\"纳智捷\",\"brand_id\":\"100\"},{\"update_time\":\"2018-11-19 14:58:01\",\"initial\":\"N\",\"brand_name\":\"哪吒汽车\",\"brand_id\":\"713\"},{\"update_time\":\"2019-07-19 11:54:43\",\"initial\":\"N\",\"brand_name\":\"NEVS国能汽车\",\"brand_id\":\"836\"},{\"update_time\":\"2018-09-05 17:42:56\",\"initial\":\"O\",\"brand_name\":\"欧拉\",\"brand_id\":\"635\"},{\"update_time\":\"2018-09-13 15:49:20\",\"initial\":\"O\",\"brand_name\":\"欧尚汽车\",\"brand_id\":\"637\"},{\"update_time\":\"2016-01-27 11:41:56\",\"initial\":\"O\",\"brand_name\":\"欧宝\",\"brand_id\":\"102\"},{\"update_time\":\"2016-01-27 11:41:57\",\"initial\":\"O\",\"brand_name\":\"欧朗\",\"brand_id\":\"103\"},{\"update_time\":\"2016-01-27 13:35:08\",\"initial\":\"O\",\"brand_name\":\"讴歌\",\"brand_id\":\"101\"},{\"update_time\":\"2018-11-23 13:49:13\",\"initial\":\"P\",\"brand_name\":\"Polestar\",\"brand_id\":\"716\"},{\"update_time\":\"2020-08-24 09:39:01\",\"initial\":\"P\",\"brand_name\":\"Pgo\",\"brand_id\":\"679\"},{\"update_time\":\"2017-01-03 17:52:24\",\"initial\":\"P\",\"brand_name\":\"帕加尼\",\"brand_id\":\"503\"},{\"update_time\":\"2016-01-27 14:28:54\",\"initial\":\"Q\",\"brand_name\":\"起亚\",\"brand_id\":\"104\"},{\"update_time\":\"2016-01-27 14:27:40\",\"initial\":\"Q\",\"brand_name\":\"奇瑞\",\"brand_id\":\"105\"},{\"update_time\":\"2021-04-07 15:52:39\",\"initial\":\"Q\",\"brand_name\":\"奇瑞途居\",\"brand_id\":\"2362\"},{\"update_time\":\"2016-01-27 13:30:41\",\"initial\":\"Q\",\"brand_name\":\"启辰\",\"brand_id\":\"106\"},{\"update_time\":\"2016-01-27 11:49:26\",\"initial\":\"Q\",\"brand_name\":\"庆铃\",\"brand_id\":\"107\"},{\"update_time\":\"2018-07-12 13:52:42\",\"initial\":\"Q\",\"brand_name\":\"乔治·巴顿\",\"brand_id\":\"624\"},{\"update_time\":\"2022-11-07 14:08:14\",\"initial\":\"Q\",\"brand_name\":\"骐铃汽车\",\"brand_id\":\"2974\"},{\"update_time\":\"2018-08-14 18:22:29\",\"initial\":\"Q\",\"brand_name\":\"前途汽车\",\"brand_id\":\"632\"},{\"update_time\":\"2016-01-27 14:22:43\",\"initial\":\"R\",\"brand_name\":\"日产\",\"brand_id\":\"108\"},{\"update_time\":\"2016-01-27 14:26:50\",\"initial\":\"R\",\"brand_name\":\"荣威\",\"brand_id\":\"109\"},{\"update_time\":\"2018-08-13 16:48:33\",\"initial\":\"R\",\"brand_name\":\"瑞驰新能源\",\"brand_id\":\"631\"},{\"update_time\":\"2016-01-27 14:14:21\",\"initial\":\"R\",\"brand_name\":\"瑞麒\",\"brand_id\":\"110\"},{\"update_time\":\"2021-04-06 17:18:04\",\"initial\":\"R\",\"brand_name\":\"瑞弗房车\",\"brand_id\":\"2360\"},{\"update_time\":\"2018-02-11 17:34:15\",\"initial\":\"R\",\"brand_name\":\"如虎\",\"brand_id\":\"570\"},{\"update_time\":\"2016-01-27 14:24:50\",\"initial\":\"S\",\"brand_name\":\"斯柯达\",\"brand_id\":\"112\"},{\"update_time\":\"2016-01-27 14:26:01\",\"initial\":\"S\",\"brand_name\":\"斯巴鲁\",\"brand_id\":\"113\"},{\"update_time\":\"2017-01-03 16:09:20\",\"initial\":\"S\",\"brand_name\":\"斯威\",\"brand_id\":\"498\"},{\"update_time\":\"2019-12-19 09:23:38\",\"initial\":\"S\",\"brand_name\":\"斯达泰克\",\"brand_id\":\"644\"},{\"update_time\":\"2016-01-27 14:20:07\",\"initial\":\"S\",\"brand_name\":\"三菱\",\"brand_id\":\"111\"},{\"update_time\":\"2019-09-10 19:38:36\",\"initial\":\"S\",\"brand_name\":\"上汽MAXUS\",\"brand_id\":\"34\"},{\"update_time\":\"2016-01-27 14:27:47\",\"initial\":\"S\",\"brand_name\":\"Smart\",\"brand_id\":\"116\"},{\"update_time\":\"2020-07-01 10:14:20\",\"initial\":\"S\",\"brand_name\":\"SERES赛力斯\",\"brand_id\":\"1689\"},{\"update_time\":\"2021-01-05 22:20:47\",\"initial\":\"S\",\"brand_name\":\"SHELBY\",\"brand_id\":\"1717\"},{\"update_time\":\"2021-06-30 18:42:53\",\"initial\":\"S\",\"brand_name\":\"SS DOLPHIN\",\"brand_id\":\"2368\"},{\"update_time\":\"2020-01-09 14:42:19\",\"initial\":\"S\",\"brand_name\":\"思皓\",\"brand_id\":\"1301\"},{\"update_time\":\"2016-03-12 17:27:01\",\"initial\":\"S\",\"brand_name\":\"思铭\",\"brand_id\":\"169\"},{\"update_time\":\"2016-01-27 13:38:40\",\"initial\":\"S\",\"brand_name\":\"双龙\",\"brand_id\":\"114\"},{\"update_time\":\"2016-01-27 12:55:56\",\"initial\":\"S\",\"brand_name\":\"双环\",\"brand_id\":\"117\"},{\"update_time\":\"2020-04-29 13:52:05\",\"initial\":\"S\",\"brand_name\":\"速达汽车\",\"brand_id\":\"1715\"},{\"update_time\":\"2016-01-26 01:14:19\",\"initial\":\"S\",\"brand_name\":\"世爵\",\"brand_id\":\"118\"},{\"update_time\":\"2016-01-26 20:25:14\",\"initial\":\"S\",\"brand_name\":\"萨博\",\"brand_id\":\"149\"},{\"update_time\":\"2020-05-19 18:32:18\",\"initial\":\"S\",\"brand_name\":\"赛麟\",\"brand_id\":\"500\"},{\"update_time\":\"2018-04-11 13:45:44\",\"initial\":\"S\",\"brand_name\":\"陕汽通家\",\"brand_id\":\"501\"},{\"update_time\":\"2019-03-21 20:09:31\",\"initial\":\"T\",\"brand_name\":\"特斯拉\",\"brand_id\":\"120\"},{\"update_time\":\"2021-11-22 18:12:46\",\"initial\":\"T\",\"brand_name\":\"坦克\",\"brand_id\":\"2414\"},{\"update_time\":\"2016-01-06 11:54:17\",\"initial\":\"T\",\"brand_name\":\"腾势\",\"brand_id\":\"166\"},{\"update_time\":\"2019-04-30 13:46:19\",\"initial\":\"T\",\"brand_name\":\"天际汽车\",\"brand_id\":\"816\"},{\"update_time\":\"2016-01-27 10:27:44\",\"initial\":\"T\",\"brand_name\":\"天马\",\"brand_id\":\"150\"},{\"update_time\":\"2019-03-28 21:47:14\",\"initial\":\"T\",\"brand_name\":\"泰赫雅特\",\"brand_id\":\"119\"},{\"update_time\":\"2022-01-12 10:23:31\",\"initial\":\"W\",\"brand_name\":\"五菱\",\"brand_id\":\"121\"},{\"update_time\":\"2016-01-27 13:20:43\",\"initial\":\"W\",\"brand_name\":\"五十铃\",\"brand_id\":\"163\"},{\"update_time\":\"2016-01-27 13:26:28\",\"initial\":\"W\",\"brand_name\":\"沃尔沃\",\"brand_id\":\"122\"},{\"update_time\":\"2017-06-21 16:28:35\",\"initial\":\"W\",\"brand_name\":\"WEY\",\"brand_id\":\"544\"},{\"update_time\":\"2018-07-07 13:55:56\",\"initial\":\"W\",\"brand_name\":\"蔚来\",\"brand_id\":\"571\"},{\"update_time\":\"2018-06-16 15:44:05\",\"initial\":\"W\",\"brand_name\":\"威马汽车\",\"brand_id\":\"617\"},{\"update_time\":\"2016-01-27 01:45:09\",\"initial\":\"W\",\"brand_name\":\"威兹曼\",\"brand_id\":\"124\"},{\"update_time\":\"2016-01-27 13:47:20\",\"initial\":\"W\",\"brand_name\":\"威麟\",\"brand_id\":\"123\"},{\"update_time\":\"2020-04-08 17:10:25\",\"initial\":\"W\",\"brand_name\":\"万丰\",\"brand_id\":\"652\"},{\"update_time\":\"2019-05-05 11:10:00\",\"initial\":\"W\",\"brand_name\":\"瓦滋汽车\",\"brand_id\":\"616\"},{\"update_time\":\"2022-11-08 16:02:52\",\"initial\":\"W\",\"brand_name\":\"万象汽车\",\"brand_id\":\"2976\"},{\"update_time\":\"2016-01-27 14:28:16\",\"initial\":\"X\",\"brand_name\":\"现代\",\"brand_id\":\"125\"},{\"update_time\":\"2016-01-27 14:29:07\",\"initial\":\"X\",\"brand_name\":\"雪佛兰\",\"brand_id\":\"126\"},{\"update_time\":\"2016-01-27 14:26:28\",\"initial\":\"X\",\"brand_name\":\"雪铁龙\",\"brand_id\":\"127\"},{\"update_time\":\"2018-12-13 09:04:00\",\"initial\":\"X\",\"brand_name\":\"小鹏汽车\",\"brand_id\":\"717\"},{\"update_time\":\"2019-04-16 18:40:14\",\"initial\":\"X\",\"brand_name\":\"星途\",\"brand_id\":\"751\"},{\"update_time\":\"2016-01-27 14:24:28\",\"initial\":\"X\",\"brand_name\":\"夏利\",\"brand_id\":\"155\"},{\"update_time\":\"2016-01-24 20:13:53\",\"initial\":\"X\",\"brand_name\":\"新凯\",\"brand_id\":\"130\"},{\"update_time\":\"2018-09-20 18:07:15\",\"initial\":\"X\",\"brand_name\":\"新特汽车\",\"brand_id\":\"638\"},{\"update_time\":\"2016-01-26 00:23:15\",\"initial\":\"X\",\"brand_name\":\"新大地\",\"brand_id\":\"151\"},{\"update_time\":\"2021-08-13 13:19:44\",\"initial\":\"X\",\"brand_name\":\"新吉奥\",\"brand_id\":\"2394\"},{\"update_time\":\"2016-01-27 10:28:31\",\"initial\":\"X\",\"brand_name\":\"新雅途\",\"brand_id\":\"148\"},{\"update_time\":\"2018-01-15 15:32:28\",\"initial\":\"X\",\"brand_name\":\"鑫源\",\"brand_id\":\"564\"},{\"update_time\":\"2016-01-27 01:46:35\",\"initial\":\"X\",\"brand_name\":\"西雅特\",\"brand_id\":\"128\"},{\"update_time\":\"2016-01-27 14:23:08\",\"initial\":\"Y\",\"brand_name\":\"英菲尼迪\",\"brand_id\":\"132\"},{\"update_time\":\"2016-01-27 13:44:28\",\"initial\":\"Y\",\"brand_name\":\"英致\",\"brand_id\":\"159\"},{\"update_time\":\"2016-01-27 14:27:35\",\"initial\":\"Y\",\"brand_name\":\"一汽\",\"brand_id\":\"131\"},{\"update_time\":\"2018-06-08 17:57:30\",\"initial\":\"Y\",\"brand_name\":\"一汽红塔\",\"brand_id\":\"615\"},{\"update_time\":\"2016-01-27 13:50:12\",\"initial\":\"Y\",\"brand_name\":\"依维柯\",\"brand_id\":\"135\"},{\"update_time\":\"2017-01-03 16:14:58\",\"initial\":\"Y\",\"brand_name\":\"野马\",\"brand_id\":\"133\"},{\"update_time\":\"2017-09-20 17:35:53\",\"initial\":\"Y\",\"brand_name\":\"御捷新能源\",\"brand_id\":\"547\"},{\"update_time\":\"2019-07-24 10:28:49\",\"initial\":\"Y\",\"brand_name\":\"远程汽车\",\"brand_id\":\"838\"},{\"update_time\":\"2018-01-30 14:09:21\",\"initial\":\"Y\",\"brand_name\":\"云度新能源\",\"brand_id\":\"565\"},{\"update_time\":\"2018-06-16 15:04:00\",\"initial\":\"Y\",\"brand_name\":\"云雀\",\"brand_id\":\"568\"},{\"update_time\":\"2021-03-30 21:31:52\",\"initial\":\"Y\",\"brand_name\":\"亚特汽车\",\"brand_id\":\"681\"},{\"update_time\":\"2021-01-05 22:20:44\",\"initial\":\"Y\",\"brand_name\":\"宇通客车\",\"brand_id\":\"643\"},{\"update_time\":\"2016-01-25 22:40:05\",\"initial\":\"Y\",\"brand_name\":\"扬州亚星客车\",\"brand_id\":\"136\"},{\"update_time\":\"2016-01-27 13:51:02\",\"initial\":\"Y\",\"brand_name\":\"永源\",\"brand_id\":\"134\"},{\"update_time\":\"2018-01-15 14:56:51\",\"initial\":\"Y\",\"brand_name\":\"裕路\",\"brand_id\":\"563\"},{\"update_time\":\"2019-05-15 16:02:38\",\"initial\":\"Y\",\"brand_name\":\"银隆新能源\",\"brand_id\":\"824\"},{\"update_time\":\"2016-01-27 14:09:28\",\"initial\":\"Z\",\"brand_name\":\"众泰\",\"brand_id\":\"138\"},{\"update_time\":\"2016-01-27 14:20:00\",\"initial\":\"Z\",\"brand_name\":\"中华\",\"brand_id\":\"137\"},{\"update_time\":\"2016-01-27 13:54:01\",\"initial\":\"Z\",\"brand_name\":\"中兴\",\"brand_id\":\"139\"},{\"update_time\":\"2021-06-17 18:30:08\",\"initial\":\"Z\",\"brand_name\":\"中国重汽VGV\",\"brand_id\":\"1076\"},{\"update_time\":\"2016-01-26 19:54:35\",\"initial\":\"Z\",\"brand_name\":\"中欧\",\"brand_id\":\"140\"},{\"update_time\":\"2016-01-26 15:11:25\",\"initial\":\"Z\",\"brand_name\":\"中客华北\",\"brand_id\":\"152\"},{\"update_time\":\"2021-01-05 22:20:45\",\"initial\":\"Z\",\"brand_name\":\"中誉\",\"brand_id\":\"660\"},{\"update_time\":\"2021-01-05 22:20:43\",\"initial\":\"Z\",\"brand_name\":\"中通客车\",\"brand_id\":\"141\"},{\"update_time\":\"2016-01-27 01:32:54\",\"initial\":\"Z\",\"brand_name\":\"中顺\",\"brand_id\":\"154\"},{\"update_time\":\"2016-02-16 15:14:18\",\"initial\":\"Z\",\"brand_name\":\"知豆\",\"brand_id\":\"168\"},{\"update_time\":\"2018-02-11 17:27:25\",\"initial\":\"Z\",\"brand_name\":\"之诺\",\"brand_id\":\"569\"},{\"update_time\":\"2021-01-05 22:20:45\",\"initial\":\"Z\",\"brand_name\":\"宗申\",\"brand_id\":\"653\"},{\"update_time\":\"2022-04-11 09:17:46\",\"initial\":\"Z\",\"brand_name\":\"智己汽车\",\"brand_id\":\"2671\"},{\"update_time\":\"2022-10-11 09:17:24\",\"initial\":\"Z\",\"brand_name\":\"自游家\",\"brand_id\":\"2966\"}]}";
//            System.out.println("获取车辆品牌信息————————" + result);

            Map maps = JSON.parseObject(result);
            for (Object map : maps.entrySet()) {
                if (((Map.Entry) map).getKey().equals("resultCode") && ((Map.Entry) map).getValue().toString().equals("0000")) {
                    for (Object mapp : maps.entrySet()) {
                        if (((Map.Entry) mapp).getKey().equals("resultData")) {
                            JSONArray jsonArray = (JSONArray) ((Map.Entry) mapp).getValue();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if (jsonObject.get("brand_name").equals(brandName)) {
                                    obj = jsonObject;
                                }
                            }
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
     * @param
     * @param brandId brandId(品牌id)
     * @param url     url （接口地址）
     *                series_id 车系 id
     *                series_name 车系名称
     *                maker_type 生产商类型
     *                series_group_name 车系组名
     *                status           状态码 1:表示成功 0:表示失败
     */

    public static Map getCarSeriesList(String brandId, String url) throws Exception {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("brandId", brandId);
        Map signMap = getSign(paramMap);

        String sign = "";
        String channel = "";
        if (!signMap.isEmpty()) {
            sign = signMap.get("sign").toString();
            channel = signMap.get("channel").toString();
        }
        StringBuffer urlParam = new StringBuffer();
        urlParam.append(url);
        urlParam.append("?sign=" + sign);
        urlParam.append("&brandId=" + brandId);
        urlParam.append("&channel=" + channel);
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
//            System.out.println("获取车系信息————————" + result);

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
     * @param
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

    public static Map getCarModelList(String seriesId, String url) throws Exception {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("seriesId", seriesId);
        StringBuffer urlParam = new StringBuffer();
        Map signMap = getSign(paramMap);

        String sign = "";
        String channel = "";
        if (!signMap.isEmpty()) {
            sign = signMap.get("sign").toString();
            channel = signMap.get("channel").toString();
        }
        urlParam.append(url);
        urlParam.append("?sign=" + sign);
        urlParam.append("&seriesId=" + seriesId);
        urlParam.append("&channel=" + channel);

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
//            System.out.println("获取车型信息————————" + result);

            maps = JSON.parseObject(result);
            getMethod.releaseConnection();
        } catch (IOException e) {
            log.error("GET请求发出失败，请求的地址为{}，参数为{}，错误信息为{}", url, getUrl, e.getMessage(), e);
        }
        return maps;
    }

    /*
     * 获取接口token
     *
     * */
    private static Map getSign(Map<String, Object> param) throws Exception {
        String channel = "tywg";
        param.put("channel", channel); //太原万国 渠道
        String sign = SignatureUtils.generateSignature(param, "B82pho2ts3HPTHvfg8JaU7cB322kTxDE");
        param.put("sign", sign);
        return param;
    }

    public static void main(String[] args) throws Exception {
        String url1 = "http://third-test.yhcs.com/getUsedCarPrice";
//        String url2 = "http://testapi.che300.com/service/pv/exportModel";
        String url3 = "http://third-test.yhcs.com/getCarBrandList";
        String url4 = "http://third-test.yhcs.com/getCarSeriesList";
        String url5 = "http://third-test.yhcs.com/getCarModelList";
        Map<String, Object> param = new HashMap<>();
        String channel = "tywg";
        param.put("channel", channel); //太原万国 渠道
//        param.put("brandId", 1);
        String sign = SignatureUtils.generateSignature(param, "B82pho2ts3HPTHvfg8JaU7cB322kTxDE");

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
        HashMap map = uctpCarInfoSearchUtils.CarFairValue("25497", "川K26D50", "12", "2016-08-31", "http://third-test.yhcs.com/getUsedCarPrice", "");
//        uctpCarInfoSearchUtils.CarMotorcycleType(url2, token, apiVersion, fromVersion, simple);
//        JSONObject obj = uctpCarInfoSearchUtils.getCarBrandList(sign, channel, "宝马", url3);
//        Map carSeriesList = uctpCarInfoSearchUtils.getCarSeriesList(token, "5", url4);
//        Map carSeriesList1 = uctpCarInfoSearchUtils.getCarModelList(token, "51618", url5);
//        System.out.println(obj.toString());
//        System.out.println(map.toString());
//        JSONObject aa = getCarBrandList("特斯拉", url3);

        System.out.println(map.toString());
    }
}
