package com.robot.code.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.robot.code.entity.RequestRecord;
import com.robot.code.mapper.RequestRecordMapper;
import com.robot.code.service.IRequestRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机器人请求的流水 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Slf4j
@Service
public class RequestRecordServiceImpl extends ServiceImpl<RequestRecordMapper, RequestRecord> implements IRequestRecordService {

    // 1发送中
    private static final int SENDING = 1;
    // 2响应成功
    private static final int SUCCESS = 2;
    // -1响应失败
    private static final int FAILURE = -1;


    @Override
    public void addRequestRecord(long recordId, long robotId, String actionCode, String externalOrderNo, String reqInfo) {
        RequestRecord record = new RequestRecord();
        record.setId(recordId);
        record.setRobotId(robotId);
        record.setActionCode(actionCode);
        record.setExternalOrderNo(externalOrderNo);
        record.setReqInfo(reqInfo);
        boolean isSave = save(record);
        if (!isSave) {
            throw new IllegalArgumentException("请求流水添加失败,{}" + reqInfo);
        }
    }

    @Async
    @Override
    public void updateRequestRecord(long recordId, boolean isSuccess, String error) {
        RequestRecord record = new RequestRecord();
        record.setId(recordId);
        record.setStatus(isSuccess ? SUCCESS : FAILURE);
        record.setError(error);
        boolean isUpdate = updateById(record);
        if (!isUpdate) {
            throw new IllegalArgumentException("请求流水更新失败,id：" + recordId + ",isSuccess:" + isSuccess);
        }
    }
}
