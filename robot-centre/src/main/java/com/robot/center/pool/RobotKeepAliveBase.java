package com.robot.center.pool;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IExecute;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.AbstractHttpClientFactory;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.entity.TenantRobot;
import com.bbin.utils.project.MyBeanUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
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
    public ResponseResult login(long robotId, TenantRobotDTO robotDTO) throws Exception{
        // 获取机器人
        ResponseResult robotResult = robotManager.getRobotById(robotId);
        if (!robotResult.isSuccess()) {
            return robotResult;
        }
        TenantRobot robot = (TenantRobot) robotResult.getObj();
        RobotWrapper robotWrapper = MyBeanUtils.copyProperties(robot, RobotWrapper.class);
        robotWrapper.setIdCard(UUID.randomUUID().toString());
        robotWrapper.setCookieStore(new BasicCookieStore());
        CloseableHttpClient httpClient = clientFactory.createHttpClient(robotId);
        // 将新httpclient放入执行器里备用
        execute.setHttpClient(robotId,httpClient);
        // 登录获取Cookie
        ResponseResult loginResult = loginExe(new ParamWrapper<TenantRobotDTO>(robotDTO),robotWrapper);
        if (!loginResult.isSuccess()) {
            return loginResult;
        }

        // 更新DB状态
        robotManager.onlineRobotByDB(robotId);

        // 入缓存
        robotWrapper.setIsOnline(true);
        robotWrapper.setInfo("运行中...");
        boolean isCacheAddSuccess = robotManager.cacheRobotAdd(robotWrapper);
        if (!isCacheAddSuccess) {
            throw new IllegalStateException("Cookie入缓存失败:robotId:" + robotId);
        }
        return loginResult;
    }

    // 自动刷新Cookie的方法

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

    /**
     * 是否机器人存活
     * @return
     */
    protected abstract boolean isRobotAlive(TenantRobot robot);
}
