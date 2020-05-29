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
     * @param recordId
     * @param robotId
     * @param pathCode
     * @param externalOrderNo
     * @param reqInfo
     * @return 流水Id
     */
    void addRequestRecord(long recordId, long robotId, String pathCode, String externalOrderNo, String reqInfo);


    /**
     * 更新流水
     * @param recordId
     * @param isSuccess
     * @param error
     */
    void updateRequestRecord(long recordId,boolean isSuccess,String error);



}
