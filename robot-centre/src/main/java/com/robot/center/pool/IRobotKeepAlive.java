package com.robot.center.pool;


import com.bbin.common.response.ResponseResult;
import com.robot.code.dto.LoginDTO;

/**
 * Created by mrt on 2019/7/6 0006 上午 10:55
 */
public interface IRobotKeepAlive {


    /**
     * 登录机器人
     * @return
     */
    ResponseResult login(LoginDTO robotDTO) throws Exception;
}
