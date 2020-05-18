package com.robot.code.service.impl;

import com.robot.code.entity.HttpRequestConfig;
import com.robot.code.mapper.HttpRequestConfigMapper;
import com.robot.code.service.IHttpRequestConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 请求配置（专业人员）
1.只为path进行配置 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Service
public class HttpRequestConfigServiceImpl extends ServiceImpl<HttpRequestConfigMapper, HttpRequestConfig> implements IHttpRequestConfigService {

}
