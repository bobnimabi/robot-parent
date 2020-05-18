package com.robot.code.service.impl;

import com.robot.code.entity.AsyncRequestConfig;
import com.robot.code.mapper.AsyncRequestConfigMapper;
import com.robot.code.service.IAsyncRequestConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 异步请求限制（一般用于支付接口） 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Service
public class AsyncRequestConfigServiceImpl extends ServiceImpl<AsyncRequestConfigMapper, AsyncRequestConfig> implements IAsyncRequestConfigService {

}
