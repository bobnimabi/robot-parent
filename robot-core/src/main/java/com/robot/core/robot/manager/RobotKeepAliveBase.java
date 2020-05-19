package com.robot.core.robot.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobot;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

/**
 * Created by mrt on 11/1/2019 8:34 PM
 */
public abstract class RobotKeepAliveBase implements IRobotKeepAlive{

    @Autowired
    private AbstractHttpClientFactory clientFactory;

    @Autowired
    private RobotManager robotManager;

    @Autowired
    private IExecute execute;

    @Transactional
    public ResponseResult login(LoginDTO robotDTO) throws Exception{
        long robotId = robotDTO.getId();
        // 获取机器人
        ResponseResult robotResult = robotManager.getRobotById(robotId);
        if (!robotResult.isSuccess()) {
            return robotResult;
        }
        RobotWrapper robotWrapper = wrapper((TenantRobot) robotResult.getObj());

        // 登录获取Cookie
        ResponseResult loginResult = loginExe(new ParamWrapper<Object>(robotDTO),robotWrapper);
        if (!loginResult.isSuccess()) {
            return loginResult;
        }
        // 更新DB状态
        robotManager.onlineRobotByDB(robotId);

//        temp(robotWrapper);
        // 入缓存
        boolean isCacheAddSuccess = robotManager.cacheRobotAdd(robotWrapper);
        if (!isCacheAddSuccess) {
            throw new IllegalStateException("Cookie入缓存失败:robotId:" + robotId);
        }
        return loginResult;
    }


    // 包装机器人
    private RobotWrapper wrapper(TenantRobot robot) {
        RobotWrapper robotWrapper = MyBeanUtil.copyProperties(robot, RobotWrapper.class);
        robotWrapper.setIdCard(UUID.randomUUID().toString());
        robotWrapper.setCookieStore(new BasicCookieStore());
        robotWrapper.setIsOnline(true);
        robotWrapper.setInfo("运行中...");
        return robotWrapper;
    }

    /**
     * 登录细节:本质获取Cookie
     *
     * @param paramWrapper
     * @return
     */
    protected abstract ResponseResult loginExe(ParamWrapper paramWrapper,RobotWrapper robotWrapper) throws Exception;

    /**
     * 获取所有的机器人
     * @return
     */
    protected abstract LambdaQueryWrapper<TenantRobot> get();

}
