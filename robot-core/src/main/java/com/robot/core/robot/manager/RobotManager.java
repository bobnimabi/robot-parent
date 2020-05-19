package com.robot.core.robot.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Created by mrt on 10/27/2019 3:05 PM
 */
@Slf4j
@Service
public class RobotManager extends AbstractRobotCache implements IRobotManager{
    private static final long INTERVAL = 100;

    public RobotWrapper getRobotInterval(Duration duration) {
        long num = (duration.toMillis() + INTERVAL - 1) / INTERVAL;
        RobotWrapper robotWrapper = null;
        for (int i = 0; i <num ; i++) {
            robotWrapper = cacheRobotGet();
            if (null != robotWrapper) {
                break;
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
            }
        }
        return robotWrapper;
    }
}
