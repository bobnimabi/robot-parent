package com.robot.center.util;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/15/2019 4:13 PM
 * 注意：响应必须接，因为每次转换都会新建BigDecimal
 */
public class MoneyUtil {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     * 分转元
     * @param fen
     * @return
     */
    public static BigDecimal convertToYuan(BigDecimal fen) {
        return fen.divide(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 元转分
     * @param yuan
     * @return
     */
    public static BigDecimal convertToFen(BigDecimal yuan) {
        return yuan.multiply(ONE_HUNDRED).setScale(0, BigDecimal.ROUND_DOWN);
    }

    /**
     * 格式化元
     * @param yuan
     * @return
     */
    public static BigDecimal formatYuan(BigDecimal yuan) {
        return yuan.setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 格式化分
     * @param fen
     * @return
     */
    public static BigDecimal formatFen(BigDecimal fen) {
        return fen.setScale(0, BigDecimal.ROUND_DOWN);
    }

    /**
     * 格式化元(字符串)
     * @param yuan
     * @return
     */
    public static BigDecimal formatYuan(String yuan) {
        return new BigDecimal(yuan).setScale(2, BigDecimal.ROUND_DOWN);
    }

}
