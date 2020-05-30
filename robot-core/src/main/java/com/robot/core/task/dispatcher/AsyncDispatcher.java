package com.robot.core.task.dispatcher;

import com.robot.code.dto.Response;
import com.robot.code.entity.AsyncRequestConfig;
import com.robot.code.service.IAsyncRequestConfigService;
import com.robot.core.common.TContext;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
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

    @Autowired
    private ITaskPool taskPool;

    @Autowired
    private IAsyncRequestConfigService asyncConfig;

    @Autowired
    private StringRedisTemplate redis;

    @Lazy
    @Autowired
    private AsyncDispatcher asyncDispatcher;
    /**
     * 注册事件
     */
    private static volatile ConcurrentHashMap<RegisterBody,String> register= new ConcurrentHashMap<>();

    @Override
    public void asyncDispatch(ParamWrapper paramWrapper, String exteralNo, IPathEnum pathEnum, IFunctionEnum functionEnum) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        AsyncRequestConfig asyncRequestConfig = asyncConfig.get(pathEnum.getpathCode());
        taskPool.taskAdd(new TaskWrapper(paramWrapper, functionEnum, asyncRequestConfig, pathEnum), exteralNo);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(asyncDispatcher).start();
    }



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
            IFunction iFunction = getFunction(functionEnum);
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



}
