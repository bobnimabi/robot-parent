package com.robot.core.robot.manager;

import com.bbin.utils.project.MyBeanUtil;
import com.robot.code.dto.Response;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.entity.TenantRobot;
import com.robot.code.service.ITenantRobotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.UUID;

/**
 * @Author mrt
 * @Date 2020/5/26 15:50
 * @Version 2.0
 */
@Slf4j
@Service
public class Manager implements IManager {

    @Autowired
    private ICloudCookie cloudCookie;

    @Autowired
    private ICloudIdCard cloudIdCard;

    @Autowired
    private ICloudTokenQueue tokenQueue;

    @Autowired
    private ITenantRobotService dbRobot;

    /**
     * 同步循环获取机器人的时间间隔，单位：毫秒
     */
    private static final long INTERVAL = 100;

    @Override
    public Response addRobot(TenantRobotDTO robotDTO) {
        return dbRobot.addRobot(robotDTO);
    }

    @Transactional
    @Override
    public Response deleteRobot(long robotId) {
        Response response = dbRobot.deleteRobot(robotId);
        this.offline(robotId);
        return response;
    }

    @Transactional
    @Override
    public Response updateRobot(TenantRobotDTO robotDTO) {
        Response response = dbRobot.updateRobot(robotDTO);
        this.offline(robotDTO.getId());
        return response;
    }

    @Override
    public Response pageRobot(TenantRobotDTO robotDTO) {
        return dbRobot.pageRobot(robotDTO);
    }

    @Override
    public Response getRobotById(long robotId) {
        return dbRobot.getRobotById(robotId);
    }

    @Transactional
    @Override
    public boolean online(RobotWrapper robotWrapper) {
        long robotId = robotWrapper.getId();
        String idCard = robotWrapper.getIdCard();
        boolean isOnlineDB = dbRobot.onlineDB(robotWrapper.getId());
        if (isOnlineDB) {
            boolean pushToken = tokenQueue.pushToken(new Token(robotId, idCard));
            if (pushToken) {
                return true;
            }
        }
        cloudIdCard.delIdCard(robotId);
        throw new IllegalArgumentException("设置Cookie失败");
    }

    @Transactional
    @Override
    public boolean offline(long robotId) {
        boolean isOfflineDB = dbRobot.offlineDB(robotId);
        if (isOfflineDB) {
            boolean isDelIdCard = cloudIdCard.delIdCard(robotId);
            if (isDelIdCard) {
                return true;
            }
        }
        throw new IllegalArgumentException("下线机器人失败，robotId:" + robotId);
    }


    @Override
    public RobotWrapper getCookie(long robotId) {
        String idCard = cloudIdCard.getIdCard(robotId);
        RobotWrapper cookie = cloudCookie.getCookie(robotId);
        if (null != cookie && !StringUtils.isEmpty(idCard)) {
            cookie.setIdCard(idCard);
            return cookie;
        }
        return null;
    }

    @Transactional
    @Override
    public void newCookie(long robotId) {
        boolean isOfflineDB = dbRobot.offlineDB(robotId);
        if (!isOfflineDB) {
            throw new IllegalArgumentException("DB：下线机器人失败");
        }
        TenantRobot robot = dbRobot.getById(robotId);
        if (null != robot) {
            RobotWrapper robotWrapper = MyBeanUtil.copyProperties(robot, RobotWrapper.class);
            robotWrapper.setCookieStore(new BasicCookieStore());
            boolean isPut = cloudCookie.putCookie(robotWrapper);
            if (isPut) {
                String s = UUID.randomUUID().toString();
                boolean setIdCard = cloudIdCard.setIdCard(robotId, s);
                if (setIdCard) {
                    return;
                }
            }
        }
        throw new IllegalArgumentException("生成新Cookie失败，robotId:" + robotId);
    }

    @Override
    public RobotWrapper popCookie() {
        Token token = tokenQueue.popToken();
        if (null != token) {
            RobotWrapper cookie = cloudCookie.getCookie(token.getRobotId());
            if (null != cookie) {
                cookie.setIdCard(token.getIdCard());
                return cookie;
            }
        }
        return null;
    }

    @Override
    public RobotWrapper getCookieDuration(Duration duration) {
        long num = (duration.toMillis() + INTERVAL - 1) / INTERVAL;
        RobotWrapper robotWrapper = null;
        int i = 0;
        do {
            robotWrapper = this.popCookie();
            if (null != robotWrapper) {
                break;
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
            }
        } while (i++ < num);
        return robotWrapper;
    }

    @Override
    public void giveBackCookieAndToken(RobotWrapper robotWrapper) {
        try {
            Long robotId = robotWrapper.getId();
            String idCard = robotWrapper.getIdCard();
            Assert.hasText(idCard,"返还Cookie：idCard为空");
            Assert.notNull(robotId,"返还Cookie：robotId为空");
            Assert.notNull(robotWrapper.getCookieStore(),"返还Cookie：CookieStore为空");
            boolean pushToken = tokenQueue.pushToken(new Token(robotId, idCard));
            if (pushToken) {
                boolean putCookie = cloudCookie.putCookie(robotWrapper);
                if (putCookie) {
                    return;
                }
            }
            throw new IllegalArgumentException("返还Cookie和Token：失败");
        } catch (IllegalArgumentException e) {
            log.error("返还Cookie和Token：失败,robotWrapper:{}",robotWrapper,e);
        }
    }

    @Override
    public void giveBackCookie(RobotWrapper robotWrapper) {
        Assert.notNull(robotWrapper.getId(),"返还Cookie：robotId为空");
        Assert.notNull(robotWrapper.getCookieStore(),"返还Cookie：CookieStore为空");
        boolean putCookie = cloudCookie.putCookie(robotWrapper);
        if (putCookie) {
            return;
        }
        throw new IllegalArgumentException("返还Cookie：失败");
    }
}
