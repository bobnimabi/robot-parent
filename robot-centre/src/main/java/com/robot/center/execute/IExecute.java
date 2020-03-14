package com.robot.center.execute;//package com.bbin.robotWrapper.core.execute;

import com.robot.center.http.CustomHeaders;
import com.robot.center.http.CustomHttpMethod;
import com.robot.center.http.ICustomEntity;
import com.robot.center.http.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;

/**
 * Created by mrt on 2019/7/10 0010 下午 4:08
 */
public interface IExecute {

    /**
     * 请求
     *
     * @param robotWrapper    机器人包装类
     * @param method          请求方法
     * @param action          动作
     * @param externalOrderNo 外部订单号
     * @param customEntity    请求体
     * @param headers         请求头
     * @param resultParse     响应结果转换
     * @return 返回null的情况
     * 1.登录状态失效
     * 2.请求无响应
     * 3.请求过程出现异常
     */
    StanderHttpResponse request(RobotWrapper robotWrapper, CustomHttpMethod method, TenantRobotAction action, String externalOrderNo,
                                ICustomEntity customEntity, CustomHeaders headers, IResultParse resultParse);

    StanderHttpResponse request(RobotWrapper robotWrapper, CustomHttpMethod method, TenantRobotAction action, String externalOrderNo,
                                ICustomEntity customEntity, CustomHeaders headers, IResultParse resultParse,boolean checkLose);
}
