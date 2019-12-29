package com.robot.jiuwu.activity.basic;

import com.robot.center.execute.AbstractExecute;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.util.BoyerMoore;
import com.robot.code.entity.TenantRobotDrop;
import com.robot.code.service.ITenantRobotDropService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

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
        // 这个的作用是提示作用，本可以不写
        List<TenantRobotDrop> drops = robotDropService.list();
        if (CollectionUtils.isEmpty(drops)) {
            log.info("未配置掉线标志，不进行掉线判定，请尽快配置");
            return false;
        }

        // 满足掉线条件才会判定掉线，否则都视为未掉线
        if (!StringUtils.isEmpty(response.getEntityStr())) {
            for (TenantRobotDrop drop : drops) {
                int result = BoyerMoore.find(drop.getDropFlag(), response.getEntityStr());
                if (-1 != result) {
                    return true;
                }
            }
        }
        return false;
    }
}
