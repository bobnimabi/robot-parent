package com.robot.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.robot.code.entity.TenantRobotRecord;

/**
 * <p>
 * 机器人打款记录流水 服务类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
public interface ITenantRobotRecordService extends IService<TenantRobotRecord> {

    /**
     * 新增机器人流水日志
     * @param id
     * @param robotId        机器人ID
     * @param externalOrderNo 外部订单号
     * @param actionCode          动作id
     * @param status          状态 1发送中 2响应成功 -1 响应失败
     * @param reqInfo         请求参数
     */
    void addRecordAsync(String id, long robotId, String externalOrderNo, String actionCode, int status, String reqInfo);

    /**
     * 更新流水
     * @param id 流水id
     * @param respInfo 响应信息
     * @param status 状态 1发送中 2响应成功 -1 响应失败
     */
    void updateRecordAsync(String id, String respInfo, int status);
}
