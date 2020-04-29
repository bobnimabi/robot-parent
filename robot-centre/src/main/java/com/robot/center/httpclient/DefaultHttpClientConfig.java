package com.robot.center.httpclient;

import com.robot.center.http.CustomHeaders;
import lombok.Data;

/**
 * Created by mrt on 10/17/2019 4:30 PM
 */
@Data
public class DefaultHttpClientConfig {

	// ------------------------连接池设置------------------------
// 连接池最大连接数
    private static final int MAX_TOTAL = 100;
    // 默认路由并发数
    private static final int MAX_PER_ROUTE = 10;
    // 无效连接校验时间，单位：秒
    private static final int VALIDATE_AFTER_INACTIVITY = 1;
    // 空闲和过期连接检查时间间隔，单位：秒
    private static final int SLEEP_TIME = 30;
    // 最大空闲时间，单位：秒
    private static final int MAX_IDLE_TIME = 30;

	// ------------------------请求设置------------------------


	// ------------------------代理设置------------------------


	// ------------------------全局请求头------------------------


}
