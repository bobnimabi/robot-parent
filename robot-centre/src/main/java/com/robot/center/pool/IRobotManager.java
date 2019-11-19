package com.robot.center.pool;

import com.robot.code.service.ITenantRobotService;

import java.time.Duration;

/**
 * Created by mrt on 10/27/2019 3:42 PM
 */
public interface IRobotManager extends IRobotCache, ITenantRobotService {
    RobotWrapper getRobotInterval(Duration duration);
}
