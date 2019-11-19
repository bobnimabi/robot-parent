package com.robot.code.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.robot.code.entity.TenantRobotRecord;
import com.robot.code.mapper.TenantRobotRecordMapper;
import com.robot.code.service.ITenantRobotRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 机器人打款记录流水 服务实现类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
@Slf4j
@Service
public class TenantRobotRecordServiceImpl extends ServiceImpl<TenantRobotRecordMapper, TenantRobotRecord> implements ITenantRobotRecordService {

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void addRecordAsync(String id, long robotId, String externalOrderNo, String actionCode, int status, String reqInfo) {
        TenantRobotRecord record = new TenantRobotRecord();
        record.setId(id);
        record.setRobotId(robotId);
        record.setExternalOrderNo(externalOrderNo);
        record.setActionCode(actionCode);
        record.setStatus(status);
        record.setReqInfo(reqInfo);
        boolean isSave = save(record);
        if (!isSave) {
            log.error("流水日志存储失败：{}", JSON.toJSONString(record));
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void updateRecordAsync(String id, String respInfo, int status) {
        log.info("recordId:{},响应信息：{}，状态：{}", id, respInfo, status);
        TenantRobotRecord record = new TenantRobotRecord();
        record.setId(id);
        record.setRespInfo(respInfo);
        record.setStatus(status);
        boolean isUpdate = updateById(record);
        if (!isUpdate) {
            log.error("流水日志更新失败：{}", JSON.toJSONString(record));
        }
    }
}
