package com.newtouch.uctp.module.business.util;
/**
 * 将null值和 "null" 替换为空字符串 ""
 *
 * */
public class NullReplaceUtil {

    public static String nullReplace(String msg){

        String message="";
        if (msg!="null"&&msg!=null){
            message=msg;
        }
        return message;
    }

    public static void main(String[] args) {
        //String s = Optional.ofNullable(String.valueOf(noticeInfoDO.getTenantId())).orElse("");

        String s ="123" == "null" ? "" : "123";
        nullReplace(s);
        System.out.println(nullReplace(s));
    }
}
