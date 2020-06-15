package com.robot.core.task.dispatcher;

import com.robot.code.entity.AsyncRequestConfig;
import com.robot.code.service.IAsyncRequestConfigService;
import com.robot.code.service.IRequestRecordService;
import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author mrt
 * @Date 2020/5/28 11:44
 * @Version 2.0
 */
@Slf4j
@Service
public class AsyncDispatcher extends AbstractDispatcher implements IAsyncDispatcher, Reactor, Runnable, InitializingBean {
    private static final String EXTERNAL_NO = RedisConsts.PROJECT + "EXTERNAL_NO:";

    @Autowired
    private ITaskPool taskPool;

    @Autowired
    private IAsyncRequestConfigService asyncConfigService;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private IRequestRecordService requestRecordService;

    @Lazy
    @Autowired
    private AsyncDispatcher asyncDispatcher;

    /**
     * 注册事件
     */
    private static volatile ConcurrentHashMap<RegisterBody,String> register= new ConcurrentHashMap<>();

    @Override
    public void asyncDispatch(ParamWrapper paramWrapper, String exteralNo, IPathEnum pathEnum, IFunctionEnum functionEnum) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Assert.notNull(paramWrapper,"paramWrapper不能为null");
        Assert.notNull(pathEnum,"pathEnumr不能为null");
        if (isRedo(exteralNo)) {
            return;
        }
        AsyncRequestConfig asyncRequestConfig = asyncConfigService.get(pathEnum.getPathCode());
        taskPool.taskAdd(new TaskWrapper(paramWrapper, functionEnum, asyncRequestConfig, pathEnum));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this).start();
    }

    @Async
    @Override
    public void registerEvents(RegisterBody registerBody) {
        register.putIfAbsent(registerBody, "");
    }

    @Override
    public void run() {
        while (true) {
            Iterator<RegisterBody> iterator = register.keySet().iterator();
            while (iterator.hasNext()) {
                RegisterBody registerBody = iterator.next();
                handTenant(registerBody);
                RobotWrapper robotWrapper = null;
                try {
                    // 从注册中剔除空队列
                    if (0 == taskPool.size()) {
                        iterator.remove();
                        continue;
                    }
                    // 获取第一个到时的任务
                    TaskWrapper firstTask = taskPool.taskSkim();
                    if (null == firstTask) {
                        continue;
                    }
                    // 未获取到可用机器人，则轮询下个注册项
                    robotWrapper = super.dispatcherFacde.popCookie();
                    if (null == robotWrapper) {
                        continue;
                    }
                    // 检查机器人是否有时间限制
                    if (LimitRobotHelper.hasLimit(firstTask, robotWrapper.getId(), redis)) {
                        super.dispatcherFacde.giveBackCookieAndToken(robotWrapper);
                        continue;
                    }
                    this.execute(robotWrapper,registerBody);
                } catch (Exception e) {
                    if (null != robotWrapper) {
                        super.dispatcherFacde.giveBackCookieAndToken(robotWrapper);
                    }
                    log.info("循环执行异常", e);

                } finally {
                    TContext.clean();
                    this.sleep(250);
                }
            }
            this.sleep(250);
        }
    }

    private void execute(RobotWrapper robotWrapper,RegisterBody registerBody) throws Exception {
        TaskWrapper taskWrapper = null;
        // 分布式并发下，这里有可能拿不出任务
        taskWrapper = taskPool.taskGet();
        if (null != taskWrapper) {
            asyncDispatcher.dispatchAsync(taskWrapper.getParamWrapper(), taskWrapper.getFunctionEnum(), robotWrapper, registerBody);
        } else {
            super.dispatcherFacde.giveBackCookieAndToken(robotWrapper);
        }
    }

    @Async
    public void dispatchAsync(ParamWrapper paramWrapper, IFunctionEnum functionEnum, RobotWrapper robotWrapper, RegisterBody registerBody) throws Exception {
        handTenant(registerBody);
        try {
            IFunction iFunction = super.getFunction(functionEnum);
            iFunction.doFunction(paramWrapper, robotWrapper);
        }finally {
            try{
                super.dispatcherFacde.giveBackCookieAndToken(robotWrapper);
            }finally {
                TContext.clean();
            }
        }
    }

    private void handTenant(RegisterBody registerBody) {
        TContext.setTenantId(registerBody.getTenantId());
        TContext.setChannelId(registerBody.getChannelId());
        TContext.setPlatformId(registerBody.getPlatformId());
        TContext.setFunction(registerBody.getFunction());
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 外部订单号重复性检查
     * @param externalNo 外部订单号
     * @return
     */
    private boolean isRedo(String externalNo) {
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
