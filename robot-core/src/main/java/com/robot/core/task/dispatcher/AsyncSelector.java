package com.robot.core.task.dispatcher;

import com.robot.core.common.RedisConsts;
import com.robot.core.common.TContext;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author mrt
 * @Date 2020/6/16 11:00
 * @Version 2.0
 */
@Service
@Slf4j
@Lazy
public class AsyncSelector extends AbstractDispatcher implements ISelector, Runnable, InitializingBean {
    /**
     * 外部订单号缓存前缀
     */
    private static final String EXTERNAL_NO = RedisConsts.PROJECT + "EXTERNAL_NO:";

    @Autowired
    private ITaskPool taskPool;
    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private AsyncTask asyncTask;

    /**
     * 注册事件
     */
    private static volatile ConcurrentHashMap<RegisterBody,String> register= new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this).start();
    }

    @Async
    @Override
    public void registerEvents(RegisterBody registerBody) {
        register.putIfAbsent(registerBody, "");
    }

    /**
     * 1.机器人拿出来后一定要归还
     * 2.不能由于异常导致循环有终止的情况
     */
    @Override
    public void run() {
        while (true) {
            Iterator<RegisterBody> iterator = register.keySet().iterator();
            while (iterator.hasNext()) {
                RobotWrapper robotWrapper = null;
                try {
                    RegisterBody registerBody = iterator.next();
                    handTenant(registerBody);
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
            asyncTask.doAssumFunction(taskWrapper.getParamWrapper(), taskWrapper.getFunctionEnum(), robotWrapper, registerBody);
        } else {
            super.dispatcherFacde.giveBackCookieAndToken(robotWrapper);
        }
    }

    public static void handTenant(RegisterBody registerBody) {
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
}
