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
public class AsyncDispatcher extends AbstractDispatcher implements IAsyncDispatcher{
    @Autowired
    private ITaskPool taskPool;

    @Autowired
    private IAsyncRequestConfigService asyncConfigService;


    @Override
    public void asyncDispatch(ParamWrapper paramWrapper, String exteralNo, IPathEnum pathEnum, IFunctionEnum functionEnum) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Assert.notNull(paramWrapper,"paramWrapper不能为null");
        Assert.notNull(pathEnum,"pathEnumr不能为null");
        if (super.isRedo(exteralNo)) {
            return;
        }
        AsyncRequestConfig asyncRequestConfig = asyncConfigService.get(pathEnum.getPathCode());
        taskPool.taskAdd(new TaskWrapper(paramWrapper, functionEnum, asyncRequestConfig, pathEnum));
    }
}
