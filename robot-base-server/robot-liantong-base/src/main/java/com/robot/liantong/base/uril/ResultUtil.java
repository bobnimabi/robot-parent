package com.robot.liantong.base.uril;

/**
 * Created by mrt on 2020/3/28 19:55
 */
public class ResultUtil {
    public static final String getJsonString(String originResult) {
        int i1 = originResult.indexOf(40);
        int i2 = originResult.lastIndexOf(41);
        return originResult.substring(i1 + 1, i2);
    }
}
