package com.robot.code.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.robot.code.entity.TenantRobotAction;
import com.robot.code.mapper.TenantRobotActionMapper;
import com.robot.code.service.ITenantRobotActionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.nio.file.InvalidPathException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2019-10-21
 */
@Service
public class TenantRobotActionServiceImpl extends ServiceImpl<TenantRobotActionMapper, TenantRobotAction> implements ITenantRobotActionService {

    @Override
    public TenantRobotAction getAction(String actionCode) {
        TenantRobotAction action = getOne(new LambdaQueryWrapper<TenantRobotAction>().eq(TenantRobotAction::getActionCode, actionCode));
        if (null == action || StringUtils.isEmpty(action.getActionUrl())) {
            throw new InvalidPathException(JSON.toJSONString(action), "未配置action或url为空");
        }
        return action;
    }
}
