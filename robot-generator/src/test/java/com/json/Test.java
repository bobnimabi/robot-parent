package com.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ArrayListMultimap;

/**
 * Created by mrt on 2019/12/30 0030 11:26
 */
public class Test {
    public static void main(String[] args) {
        ArrayListMultimap<String,String> arrayListMultimap =  ArrayListMultimap.create();
        arrayListMultimap.put("1", "1");
        arrayListMultimap.put("1", "2");

        System.out.println(JSON.toJSONString(arrayListMultimap));
    }
}
