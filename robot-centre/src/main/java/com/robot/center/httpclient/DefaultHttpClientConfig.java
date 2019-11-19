package com.robot.center.httpclient;

import lombok.Data;

/**
 * Created by mrt on 10/17/2019 4:30 PM
 */
@Data
public class DefaultHttpClientConfig {

	// ------------------------连接池设置------------------------
 	// 连接池最大连接数
	private int maxTotal = 100;
	// 默认路由并发数
	private int defaultMaxPerRoute = 10;
	// 无效连接校验时间，单位：秒
	private int validateAfterInactivity = 1;
	// 空闲和过期连接检查时间间隔，单位：秒
	private int sleepTime = 30;
	// 最大空闲时间，单位：秒
	private int maxIdleTime = 30;

	// ------------------------请求设置------------------------
	// 从连接池获取连接超时，单位：秒
	private int connectionRequestTimeout = 5;
	// 连接超时，单位：秒
	private int connectTimeout = 10;
	// 请求时间，单位：秒
	private int socketTimeout = 20;
	// 重试次数
	private int retryCount = 3;
	// keep-alive 超时时间，单位：秒
	private int keepAliveTimeout = 30;

	// ------------------------代理设置------------------------
	// 代理IP
	private String proxyIp = null;
	// 代理端口
	private String proxyPort = null;

	// ------------------------全局请求头------------------------
	private CustomHeaders commonHeaders = new CustomHeaders();

}
