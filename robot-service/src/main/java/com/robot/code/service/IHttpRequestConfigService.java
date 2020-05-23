package com.robot.code.service;

import com.robot.code.entity.HttpRequestConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 请求配置（专业人员）
1.只为path进行配置 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface IHttpRequestConfigService extends IService<HttpRequestConfig> {
    HttpRequestConfig getConfigById(long id);


}
