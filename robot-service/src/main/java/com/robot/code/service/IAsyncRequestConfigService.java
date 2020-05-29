package com.robot.code.service;

import com.robot.code.entity.AsyncRequestConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-29
 */
public interface IAsyncRequestConfigService extends IService<AsyncRequestConfig> {


    /**
     * 通过pathCode来获取异步配置
     * @param pathCode
     * @return
     */
    AsyncRequestConfig get(String pathCode);

}
