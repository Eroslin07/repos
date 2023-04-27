package com.newtouch.uctp.module.business.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.List;
import java.util.Map;

/**
 * 长链接转短链接
 */
public class ShortUrlsUtil {

    private static final String token = "a03c080908fccf5a2a21c125abf4ded6";

    public static String shortUrls(List<String> longUrls){
        if (CollUtil.isEmpty(longUrls)) {
            return null;
        }
        Map<String, String> map = MapUtil.builder("LongUrl", "https://expose.qiyuesuo.cn/enterpriseAuth/index?token=dUpRZTJOR2Z1NTN1MWhQbURwV2tFQ1NtWEM2cE5TanAzMzRscDhxVjhzOXJ1ck8yaDhVcmQ2L1kzNjloZFFNVQ==").put("TermOfValidity", "1-year").build();
        List<Map<String, String>> list = ListUtil.of(map);
        String result = HttpRequest.post(baiduUrl())
                .header(Header.CONTENT_TYPE, "application/json; charset=UTF-8")//头信息，多个头信息多次调用此方法即可
                .header("Dwz-Token",token)
                .body(JSONUtil.toJsonStr(list))
                .timeout(20000)//超时，毫秒
                .execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(result);
        return "";
    }

    public static String baiduUrl(){
       return UrlBuilder.create()
                .setScheme("https")
                .setHost("dwz.cn")
                .addPath("//api/v3/short-urls")
                .build();
    }

}
