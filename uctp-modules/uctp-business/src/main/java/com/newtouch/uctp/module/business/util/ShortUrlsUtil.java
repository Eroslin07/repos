package com.newtouch.uctp.module.business.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import javax.sql.rowset.serial.SerialException;
import java.util.List;
import java.util.Map;

/**
 * 长链接转短链接
 */
public class ShortUrlsUtil {

    private static final String token = "a03c080908fccf5a2a21c125abf4ded6";

    public static List<String> shortUrls(List<String> longUrls){
        if (CollUtil.isEmpty(longUrls)) {
            return null;
        }
        List<String> retList = ListUtil.list(false);
        List<Map<String, String>> list = ListUtil.list(false);
        for (String longUrl : longUrls) {
            Map<String, String> map = MapUtil
                    .builder("LongUrl", "https://expose.qiyuesuo.cn/enterpriseAuth/index?token=dUpRZTJOR2Z1NTN1MWhQbURwV2tFQ1NtWEM2cE5TanAzMzRscDhxVjhzOXJ1ck8yaDhVcmQ2L1kzNjloZFFNVQ==")
                    .put("TermOfValidity", "1-year")
                    .build();
            list.add(map);
        }
        String result = HttpRequest.post(baiduUrl())
                .header(Header.CONTENT_TYPE, "application/json; charset=UTF-8")//头信息，多个头信息多次调用此方法即可
                .header("Dwz-Token",token)
                .body(JSONUtil.toJsonStr(list))
                .timeout(20000)//超时，毫秒
                .execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(result, true);
        if (ObjectUtil.equals(jsonObject.getInt("Code"),0)
                || ObjectUtil.equals(jsonObject.getInt("Code"),-99)) {
            JSONArray shortUrls = jsonObject.getJSONArray("ShortUrls");
            for (int i = 0; i < shortUrls.size(); i++) {
                JSONObject obj = shortUrls.getJSONObject(i);
                if (obj.containsKey("ShortUrl")) {
                    retList.add(obj.getStr("ShortUrl"));
                }
            }
        }  else {
            String msg = "转短链接失败，code:%s,ErrMsg:%s";
            throw new UnsupportedOperationException(
                    String.format(msg,jsonObject.getInt("Code"),
                            jsonObject.getStr("ErrMsg")));
        }
        return retList;
    }

    public static String baiduUrl(){
       return UrlBuilder.create()
                .setScheme("https")
                .setHost("dwz.cn")
                .addPath("//api/v3/short-urls")
                .build();
    }

    public static void main(String[] args) {
        List<String> urls = shortUrls(ListUtil.of("https://expose.qiyuesuo.cn/enterpriseAuth/index?token=V3dXb2ZOeElsSHRKNkxUT0dWQnpwYWpueng3Kzk3cmdtV2MyZUpETXJnakVwRmI4UklZOVpFT2cvaGNyWm53Vg==",
                "https://auth.qiyuesuo.cn?ticket=kkekOx5IQ25RuoEVrniCoGDThAUUPVKQgPmRrE9ch4%2F2xyndsZ4oCsurvvoMampa&channel=SAAS_PC"));
        System.out.println(JSONUtil.toJsonStr(urls));
    }
}
