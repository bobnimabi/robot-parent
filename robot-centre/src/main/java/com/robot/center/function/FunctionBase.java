package com.robot.center.function;

import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IExecute;
import com.robot.center.pool.IRobotManager;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.code.service.ITenantRobotActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Duration;

/**
 * Created by mrt on 11/14/2019 8:26 PM
 */
@Slf4j
public abstract class FunctionBase<T> implements IFunction<T>{

    @Autowired
    protected IExecute execute;
    @Autowired
    private IRobotManager robotManager;
    @Autowired
    private ITenantRobotActionService actionService;

    // 获取时长3秒
    private static final long PERIOD = 3L;

    @Override
    public ResponseResult doFunction(ParamWrapper<T> paramWrapper) throws Exception {
        return doFunction(paramWrapper, null, null,true);
    }

    @Override
    public ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        return doFunction(paramWrapper, robotWrapper, null, false);
    }

    @Override
    public ResponseResult doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action,Boolean isGiveBack) throws Exception {
        /**
         * robotWrapper != null的场景
         * 1.登录或图片验证码(会从数据库获取机器人)
         * 2.异步调用(会从队列提前获取机器人)
         */
        if (null == robotWrapper) {
            robotWrapper = getRobotDefaultInterval();
            if (null == robotWrapper) {
                log.error("无可用机器人或繁忙中...");
                return ResponseResult.FAIL("无可用机器人或繁忙中");
            }
        }
        /**
         * 1.异步：自己保证机器人一定归还
         * 2.同步：上面刚获取到，在这里课程保证机器人一定归还
         * 3.登录或图片验证码：机器人从表里获取的，无需归还
         */
        try {
            return doAction(paramWrapper, robotWrapper, action);
        } finally {
            if (isGiveBack && null != robotWrapper) {
                robotManager.cacheGiveBack(robotWrapper);
            }
        }
    }

    /**
     * 将获取action放在获取robot下面的考虑是
     * 1.机器人一定要归还（当需归还的时候）
     */
    private  ResponseResult doAction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception{
        IActionEnum actionEnum = null;
        // 获取Action
        if (null == action) {
            actionEnum = getActionEnum();
            if (null != actionEnum) {
                action = getAction(actionEnum);
                if (null == action) {
                    log.error("未配置Action...");
                    return ResponseResult.FAIL("未配置Action...");
                }
            }
        }
        if (null != action) {
            log.info("-------------------{}-------------------", action.getActionCode());
        }
        return doFunctionFinal(paramWrapper, robotWrapper, action);
    }

    /**
     * 获取机器人
     */
    protected RobotWrapper getRobotDefaultInterval() {
        return robotManager.getRobotInterval(Duration.ofSeconds(PERIOD));
    }

    /**
     * 获取动作
     */
    protected TenantRobotAction getAction(IActionEnum actionEnum) {
        return actionService.getAction(actionEnum.getActionCode());
    }

    /**
     * 执行功能
     * 注意：这里之所以用public是为了支持异步调用
     * @param paramWrapper
     * @param robotWrapper
     * @return
     * @throws Exception
     */
    protected abstract ResponseResult doFunctionFinal(ParamWrapper<T> paramWrapper,RobotWrapper robotWrapper,TenantRobotAction action) throws Exception;

    /**
     * 获取动作编码
     * @return
     */
    public abstract IActionEnum getActionEnum();

}
