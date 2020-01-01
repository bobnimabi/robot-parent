package com.robot.jiuwu.base.basic;

import com.alibaba.fastjson.JSON;
import com.robot.center.execute.AbstractExecute;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.code.service.ITenantRobotDropService;
import com.robot.jiuwu.base.common.Constant;
import com.robot.jiuwu.base.vo.ParentResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 7:17 PM
 */
@Slf4j
@Service
public class Executor extends AbstractExecute {
    @Autowired
    private ITenantRobotDropService robotDropService;

    @Override
    protected boolean isLose(StanderHttpResponse response) {
        String entityStr = response.getEntityStr();
        if (StringUtils.isEmpty(entityStr)) {
            return false;
        }

        ParentResultVO parentResultVO = JSON.parseObject(entityStr, ParentResultVO.class);
        if (Constant.LOSE.equals(parentResultVO.getCode())) {
            return true;
        }
        return false;
    }
}
