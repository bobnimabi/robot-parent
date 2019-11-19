package com.robot.center.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * boyer-moore 字符串匹配算法
 */
public class BoyerMoore {

    public static void main(String[] args) {
        String text = "中国是111a一个伟大的国度；伟大的祖国啊";
        String pattern = "伟大的国度";
        BoyerMoore bm = new BoyerMoore();
        int index = bm.find(pattern, text);
        System.out.println("查找到：" + index);
    }

    public static int find(String pattern, String text) {
        if (StringUtils.isEmpty(pattern) || StringUtils.isEmpty(text)) {
            return -1;
        }
        int m = pattern.length();
        int n = text.length();
        Map<String, Integer> bmBc = new HashMap<String, Integer>();
        int[] bmGs = new int[m];
        //proprocessing
        preBmBc(pattern, m, bmBc);
        preBmGs(pattern, m, bmGs);
        //searching
        int j = 0;
        int i = 0;
        int count = 0;
        while (j <= n - m) {
            for (i = m - 1; i >= 0 && pattern.charAt(i) == text.charAt(i + j); i--) {   //用于计数
                count++;
            }
            if (i < 0) {
                // 这里可以取多个值
                return j;
//                j += bmGs[0];
            } else {
                j += Math.max(bmGs[i], getBmBc(String.valueOf(text.charAt(i + j)), bmBc, m) - m + 1 + i);
            }
        }
        return -1;
    }
    private static void preBmBc(String pattern, int patLength, Map<String, Integer> bmBc) {
        for (int i = patLength - 2; i >= 0; i--) {
            if (!bmBc.containsKey(String.valueOf(pattern.charAt(i)))) {
                bmBc.put(String.valueOf(pattern.charAt(i)), (Integer) (patLength - i - 1));
            }
        }
    }

    private static void suffix(String pattern, int patLength, int[] suffix) {
        suffix[patLength - 1] = patLength;
        int q = 0;
        for (int i = patLength - 2; i >= 0; i--) {
            q = i;
            while (q >= 0 && pattern.charAt(q) == pattern.charAt(patLength - 1 - i + q)) {
                q--;
            }
            suffix[i] = i - q;
        }
    }

    private static void preBmGs(String pattern, int patLength, int[] bmGs) {
        int i, j;
        int[] suffix = new int[patLength];
        suffix(pattern, patLength, suffix);
        //模式串中没有子串匹配上好后缀，也找不到一个最大前缀  
        for (i = 0; i < patLength; i++) {
            bmGs[i] = patLength;
        }
        //模式串中没有子串匹配上好后缀，但找到一个最大前缀  
        j = 0;
        for (i = patLength - 1; i >= 0; i--) {
            if (suffix[i] == i + 1) {
                for (; j < patLength - 1 - i; j++) {
                    if (bmGs[j] == patLength) {
                        bmGs[j] = patLength - 1 - i;
                    }
                }
            }
        }
        //模式串中有子串匹配上好后缀  
        for (i = 0; i < patLength - 1; i++) {
            bmGs[patLength - 1 - suffix[i]] = patLength - 1 - i;
        }
    }

    private static int getBmBc(String c, Map<String, Integer> bmBc, int m) {
        //如果在规则中则返回相应的值，否则返回pattern的长度  
        if (bmBc.containsKey(c)) {
            return bmBc.get(c);
        } else {
            return m;
        }
    }
}

