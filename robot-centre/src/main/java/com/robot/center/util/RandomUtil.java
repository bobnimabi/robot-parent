package com.robot.center.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.util.Random;

public class RandomUtil {
    private static final char[] NUMBER_SEED = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private static final char[] COMMON_SEED = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', 'A','B','C','D','E','F','G','H','I','J','K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y' ,'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };


    /**
     * 创建数字随机数
     * @param len
     * @return
     */
    public static String randomNum(int len) {
        return createRandomNumberStr(NUMBER_SEED, len);
    }

    /**
     * 创建数字随机数
     * @param len
     * @return
     */
    public static String randomString(int len) {
        return createRandomNumberStr(COMMON_SEED, len);
    }


    private static String createRandomNumberStr(char[] str,int len) {
        Random rd = new Random();
        final int maxNum = str.length;
        StringBuffer sb = new StringBuffer(len);
        int index;//取得随机数
        int count = 0;
        while (count++ < len) {
            index = Math.abs(rd.nextInt(maxNum));
            sb.append(str[index]);
        }
        return sb.toString();
    }
}