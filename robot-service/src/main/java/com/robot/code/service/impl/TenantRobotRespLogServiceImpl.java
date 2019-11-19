package com.robot.code.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.robot.code.entity.TenantRobotRespLog;
import com.robot.code.mapper.TenantRobotRespLogMapper;
import com.robot.code.service.ITenantRobotRespLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
@Slf4j
@Service
public class TenantRobotRespLogServiceImpl extends ServiceImpl<TenantRobotRespLogMapper, TenantRobotRespLog> implements ITenantRobotRespLogService {

    @Async
    @Override
    public void saveRespRecordAsync(String recordId, String respContent) {
        TenantRobotRespLog respLog = new TenantRobotRespLog();
        respLog.setId(IdWorker.getIdStr());
        respLog.setRecordId(recordId);
        respLog.setRespContent(respContent);
        boolean isSave = save(respLog);
        if (!isSave) {
            log.error("存储响应日志失败:{}", JSON.toJSONString(respLog));
        }
    }
}
