package com.robot.center.util;

import java.time.format.DateTimeFormatter;

/**
 * <p>
 *
 * </p>
 *
 * @author tank
 * @date 2020/7/16
 */
public class DateUtil {
	public static final DateTimeFormatter HOUR_MIN_ONE = DateTimeFormatter.ofPattern("H:mm");
	public static final DateTimeFormatter HOUR_MIN_TWO = DateTimeFormatter.ofPattern("HH:mm");
	public static final DateTimeFormatter YEAR_MONTH_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter HOUR_MIN_SEC = DateTimeFormatter.ofPattern("HH:mm:ss");
	public static final DateTimeFormatter YEAR_MONTH_DAY_MORE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter MONTH_DAY_MORE = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");
}
