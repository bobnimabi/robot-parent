package com.robot.center.dispatch;

import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.function.IFunction;
import com.robot.center.pool.RobotManager;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.TContext;
import com.robot.code.entity.TenantRobotAction;
import com.robot.code.service.ITenantRobotActionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by mrt on 10/31/2019 12:29 PM
 *
 */
@Slf4j
@Service
public class ReactorImpl implements Reactor {
    private static volatile ConcurrentHashMap<RegisterBody,String> register= new ConcurrentHashMap<>();
    @Autowired
    private ITenantRobotActionService actionService;
    @Autowired
    private ITaskPool taskPool;
    @Autowired
    private RobotManager robotManager;
    @Autowired
    private RobotTimeLimit timeLimit;
    @Autowired
    private Map<String, IFunction> functionMap;

    @Override
    @PostConstruct
    public void handleEvents() {
        new Thread(new PayThread()).start();
    }

    private class PayThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                Iterator<RegisterBody> iterator = register.keySet().iterator();
                while (iterator.hasNext()) {
                    RegisterBody registerBody = iterator.next();
                    try {
                        handTenant(registerBody);
                        // 从注册表中剔除空队列
                        if (0 == taskPool.size()) {
                            iterator.remove();
                            continue;
                        }
                        // 任务队列第一个任务执行时间未到，则轮询下个注册项
                        TaskWrapper taskWrapper = taskPool.taskSkim();
                        if (null == taskWrapper) {
                            continue;
                        }
                        // 未获取到可用机器人，则轮询下个注册项
                        RobotWrapper robotWrapper = robotManager.cacheRobotGet();
                        if (null == robotWrapper) {
                            continue;
                        }
                        try {
                            IFunction iFunction = functionMap.get(taskWrapper.getFunctionEnum().getFunctionServer());
                            Assert.notNull(iFunction,"未获取到IFunction,");
                            TenantRobotAction action = actionService.getAction(iFunction.getActionEnum().getActionCode());
                            // 机器人还存在限制的情况下(IP,接口时间限制等)，则轮询下个注册项
                            boolean isExecute = timeLimit.isExecute(action, robotWrapper);
                            if (!isExecute) {
                                continue;
                            }
                            // 分布式并发下，这里有可能拿不出任务
                            TaskWrapper taskWrapperGo = taskPool.taskGet();
                            if (null != taskWrapperGo) {
                                iFunction.doFunction(taskWrapperGo.getParamWrapper(), robotWrapper, action, false);
                            }
                        } catch (Exception e) {
                            log.info("单执行异常", e);
                        } finally { // 保证归还拿出来的机器人
                            if (null != robotWrapper) {
                                robotManager.cacheGiveBack(robotWrapper);
                                robotWrapper = null;
                            }
                        }
                    } catch (Exception e) {
                        log.info("循环执行异常", e);
                    } finally {
                        TContext.clean();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

    private void handTenant(RegisterBody registerBody) {
        TContext.setTenantId(registerBody.getTenantId());
        TContext.setChannelId(registerBody.getChannelId());
        TContext.setPlatformId(registerBody.getPlatformId());
        TContext.setFunction(registerBody.getFunction());
        // log4j2设置租户日志
        ThreadContext.put("TENANT_ID", TContext.getTenantId()+"");
        ThreadContext.put("CHANNEL_ID", TContext.getChannelId()+"");
        ThreadContext.put("PLATFORM_ID", RobotConsts.PLATFORM_ID.JIU_WU_CARD + "");
        ThreadContext.put("FUNCTION_CODE", RobotConsts.FUNCTION_CODE.ACTIVITY + "");
    }

    @Override
    public void registerEvents(RegisterBody registerBody) {
        Assert.notNull(registerBody,"注册:体为空");
        Assert.notNull(registerBody.getTenantId(),"注册:TenantId为空");
        Assert.notNull(registerBody.getChannelId(),"注册:ChannelId为空");
        Assert.notNull(registerBody.getPlatformId(),"注册:PlatformId为空");
        Assert.notNull(registerBody.getFunction(),"注册:Function为空");
        register.putIfAbsent(registerBody, "");
    }
}
