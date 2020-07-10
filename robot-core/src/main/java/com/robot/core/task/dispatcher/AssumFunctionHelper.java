package com.robot.core.task.dispatcher;

import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.IFunctionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author mrt
 * @Date 2020/6/17 10:31
 * @Version 2.0
 */
@Slf4j
@Service
public class AssumFunctionHelper {

    @Autowired
    private Map<String, IAssemFunction> functionMap;

    /**
     * 获取Function
     *
     * @param functionEnum
     * @return
     */
    protected final IAssemFunction getFunction(IFunctionEnum functionEnum) {
        IAssemFunction iFunction = functionMap.get(functionEnum.getName());

        if (null == iFunction) {
            throw new IllegalArgumentException("Dispatcher：未获取到Function，请管理员检查,FunctionName:" + functionEnum.getName());
        }
        return iFunction;
    }
}
