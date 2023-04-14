package com.newtouch.uctp.module.business.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtil {



    /***
     * 分批次取的list
     */
    private static int number = 100;

    /**
     * 如果list集合中有几十万条数据,每100条为一组取出
     * @param list 可穿入几十万条数据的List
     * @return map 每一key中有number条数据的List
     * @author hx
     */
    public static Map<String,List<String>>groupList(List list){
        if (list == null || list.size() == 0){
            return new HashMap();
        }
        int listSize=list.size();
        int toIndex=number;
        Map map = new HashMap();            //用map存起来新的分组后数据
        int keyToken = 0;
        for(int i = 0;i<list.size();i+=number){
            if(i+number>listSize){        //作用为toIndex最后没有100条数据则剩余几条newList中就装几条
                toIndex=listSize-i;
            }
            List newList = list.subList(i,i+toIndex);
            map.put("keyName"+keyToken, newList);
            keyToken++;
        }
        return map;
    }


}

