package com.robot.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.robot.code.entity.TenantRobotRespLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
public interface ITenantRobotRespLogService extends IService<TenantRobotRespLog> {
    /**
     * 异步存储完整的、未解析的响应参数
     * @param recordId 记录id
     * @param respContent 响应完整内容
     */
    void saveRespRecordAsync(String recordId,String respContent);

}
