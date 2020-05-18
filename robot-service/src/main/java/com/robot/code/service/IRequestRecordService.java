package com.robot.code.service;

import com.robot.code.entity.RequestRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 机器人请求的流水 服务类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
public interface IRequestRecordService extends IService<RequestRecord> {
    /**
     * 增加流水
     * @param robotId
     * @param actionCode
     * @param externalOrderNo
     * @param reqInfo
     * @return 流水Id
     */
    long addRequestRecord(long robotId,String actionCode,String externalOrderNo,String reqInfo);


    /**
     * 更新流水
     * @param id
     * @param status
     */
    void updateRequestRecord(long id,Integer status);



}
