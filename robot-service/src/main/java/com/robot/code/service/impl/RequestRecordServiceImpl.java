package com.robot.code.service.impl;

import com.robot.code.entity.RequestRecord;
import com.robot.code.mapper.RequestRecordMapper;
import com.robot.code.service.IRequestRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机器人请求的流水 服务实现类
 * </p>
 *
 * @author admin
 * @since 2020-05-18
 */
@Service
public class RequestRecordServiceImpl extends ServiceImpl<RequestRecordMapper, RequestRecord> implements IRequestRecordService {

}
