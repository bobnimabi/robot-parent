package com.robot.core.robot.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.robot.code.dto.Response;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.entity.TenantRobot;
import com.robot.code.service.ITenantRobotService;
import com.robot.code.vo.TenantRobotVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author mrt
 * @Date 2020/5/26 15:50
 * @Version 2.0
 */
public class ManagerFacde implements IManagerFacde {
    @Autowired
    private ICloudCookie cloudCookie;

    @Autowired
    private ICloudIdCard cloudIdCard;

    @Autowired
    private ICloudTokenQueue tokenQueue;

    @Autowired
    private ITenantRobotService dbRobot;

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
        return cloudCookie.getCookie(robotId);
    }

    @Override
    public RobotWrapper popCookie() {
        Token token = tokenQueue.popToken();
        return cloudCookie.getCookie(token.getRobotId());
    }
}
