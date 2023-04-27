package com.newtouch.uctp.module.business.util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.annotation.Resource;


public class MsgSendUtil {


        public static String sendPostRequest( Map<String ,String > map) {
            String response = "";

            //params = "apikey=" + URLEncoder.encode("5688b8cbdc987dbfe5cb4661e941a84c", "UTF-8") + "&text=" + URLEncoder.encode(text, "UTF-8")+ "&mobile=" + URLEncoder.encode("18942820534", "UTF-8");
            String params = "";
            Map<String, String> content = MsgContentUtil.getContent(map);

            try {
                params= "apikey=" + URLEncoder.encode("5688b8cbdc987dbfe5cb4661e941a84c", "UTF-8") + "&text=" + URLEncoder.encode(content.get("content"), "UTF-8")+ "&mobile=" + URLEncoder.encode(map.get("phone"), "UTF-8");

            } catch (UnsupportedEncodingException e) {


            }


            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost();
                httpPost.setURI(new URI("https://sms.yunpian.com/v2/sms/single_send.json"));

                StringEntity entity = new StringEntity(params, StandardCharsets.UTF_8);
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);

                try (CloseableHttpResponse httpResponse = client.execute(httpPost)) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    response = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                    EntityUtils.consume(httpEntity);
                }
            } catch (URISyntaxException | UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }


    public static Map<String ,String > sendMessage(Map<String,String> map){
        Map<String,String> MsgMap =new HashMap<>();
        String response = sendPostRequest( map);
        Boolean flag=true;
        int i=0;
        String msg="";
        String flags="TRUE";
        while (flag) {
            JSONObject jsonObject =JSON.parseObject(response);
            jsonObject.getString("code");
            if (jsonObject.get("code").equals("0") ) {
                break;
            }else {
                response= sendPostRequest(map);
                i++;
                if (i==5){
                    msg= jsonObject.getString("detail");
                    MsgMap.put("msg",msg);
                    MsgMap.put("errorNum", String.valueOf(i));
                    flags="FALSE";
                    break;

                }
            }
        }
        MsgMap.put("flags",flags);
        System.out.println(response);
        System.out.println(msg+"----"+i);
        return MsgMap;
    }

        public static void main(String[] args) throws UnsupportedEncodingException {
            String url = "https://sms.yunpian.com/v2/sms/single_send.json";
            String text ="【翼龙科技】您的#type#车合同#contactNo#，金额#amount#元，#typer#签署已超过10分钟，仍未签字，请及时与#typer#沟通处理。";
            String params = "apikey=" + URLEncoder.encode("5688b8cbdc987dbfe5cb4661e941a84c", "UTF-8") + "&text=" + URLEncoder.encode(text, "UTF-8")+ "&mobile=" + URLEncoder.encode("18942820534", "UTF-8");
            Map maps=new HashMap();
            maps.put("text","【翼龙科技】您的#type#车合同#contactNo#，金额#amount#元，#typer#签署已超过10分钟，仍未签字，请及时与#typer#沟通处理。");
            //手机号
            maps.put("phone","1894534");
            //短信
            maps.put("type","1");
            //内容模版
            maps.put("contentType","12");
            sendMessage(maps);
            /*String response = sendPostRequest(maps);


            Boolean flag=true;
            int i=0;
            String msg="";
            while (flag) {
                JSONObject jsonObject =JSON.parseObject(response);
                jsonObject.getString("code");
                if (jsonObject.get("code").equals("0") ) {
                    break;
                }else {
                    response= sendPostRequest( maps);
                    i++;
                    if (i==5){
                        msg= jsonObject.getString("detail");
                        break;

                    }
                }
            }*/


            //System.out.println(response);
        }


}

