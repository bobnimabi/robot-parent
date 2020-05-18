package com.robot.code.service;

import com.robot.code.entity.TenantRobotProxy;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 请求代理
1.可以为机器人配置
2.可以为path配置
注意：单独拆分proxy表，是因为未来在后台进行大量的代理配置 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface ITenantRobotProxyService extends IService<TenantRobotProxy> {

}
