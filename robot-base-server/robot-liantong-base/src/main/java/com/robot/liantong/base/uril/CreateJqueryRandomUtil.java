package com.robot.liantong.base.uril;

import com.robot.center.util.RandomUtil;

/**
 * Created by mrt on 2020/3/28 19:16
 */
public class CreateJqueryRandomUtil {
    public static String doRandom() {
        String reqTime = System.currentTimeMillis() + "";
        return  new StringBuilder(40)
                .append("jQuery")
                .append(RandomUtil.randomNum(20))
                .append("_")
                .append(reqTime).toString();
    }

}
