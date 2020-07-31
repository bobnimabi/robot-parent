package com.robot.core.task.dispatcher;

import com.robot.code.response.Response;
import com.robot.code.service.IRequestRecordService;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.IDispatcherFacde;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

/**
 * @Author mrt
 * @Date 2020/5/28 11:47
 * @Version 2.0
 */
@Slf4j
public abstract class AbstractDispatcher {
    private static final String EXTERNAL_NO = RedisConsts.PROJECT + "EXTERNAL_NO:";
    @Autowired
    private AssumFunctionHelper assumFunctionHelper;

    @Autowired
    protected IDispatcherFacde dispatcherFacde;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private IRequestRecordService requestRecordService;

    /**
     * 调用Function执行功能，并返回机器人
     *
     * @param paramWrapper
     * @param functionEnum
     * @param robotWrapper
     * @return
     * @throws Exception
     */
    protected final Response dispatch(ParamWrapper paramWrapper, IFunctionEnum functionEnum, RobotWrapper robotWrapper) throws Exception {
        IAssemFunction iFunction = assumFunctionHelper.getFunction(functionEnum);
        return iFunction.doFunction(paramWrapper, robotWrapper);
    }


    /**
     * 外部订单号重复性检查
     * @param externalNo 外部订单号
     * @return
     */
    protected boolean isRedo(String externalNo) {
        // redis检查
        String cacheKey = createExteralNoCacheKey(externalNo);
        Boolean isSave = redis.opsForValue().setIfAbsent(cacheKey, "", Duration.ofDays(3));
        if (!isSave) {
            log.info("redis:该外部订单已经存在：{}", externalNo);
            return true;
        }

        // mysql检查
        boolean isRepeate = requestRecordService.isRepeate(externalNo);
        if (isRepeate) {
            log.info("mysql:该外部订单已经存在：{}", externalNo);
            return true;
        }
        return false;
    }

    // 组装redis-key：外部订单号
    private String createExteralNoCacheKey(String externalNo) {
        return new StringBuilder(30)
                .append(EXTERNAL_NO)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(externalNo)
                .toString();
    }
}
