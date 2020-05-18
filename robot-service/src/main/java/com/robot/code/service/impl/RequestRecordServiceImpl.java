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

    @Override
    public long addRequestRecord(long robotId, String actionCode, String externalOrderNo, String reqInfo) {
        long idStr = IdWorker.getId();
        RequestRecord record = new RequestRecord();
        record.setId(idStr);
        record.setRobotId(robotId);
        record.setActionCode(actionCode);
        record.setExternalOrderNo(externalOrderNo);
        record.setReqInfo(reqInfo);
        boolean isSave = save(record);
        if (!isSave) {
            throw new IllegalArgumentException("请求流水添加失败,{}" + reqInfo);
        }
        return idStr;
    }

    @Async
    @Override
    public void updateRequestRecord(long id, Integer status) {
        RequestRecord record = new RequestRecord();
        record.setId(id);
        record.setStatus(status);
        boolean isUpdate = updateById(record);
        if (!isUpdate) {
            throw new IllegalArgumentException("请求流水更新失败,id：" + id + ",status:" + status);
        }
    }
}
