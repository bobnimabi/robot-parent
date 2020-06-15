package com.robot.core.task.dispatcher;//package com.bbin.robotWrapper.core.schedue;


import com.robot.code.dto.Response;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.ParamWrapper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by mrt on 2019/7/11 0011 下午 6:35
 */
public interface IAsyncDispatcher {

    /**
     * 轮询异步分发
     *
     * @param paramWrapper 请求参数包装
     * @param exteralNo        外部订单号
     * @param actionEnum   异步需要时间限制的的路径枚举，比如查询余额+打款，此时应传入打款的枚举
     * @param functionEnum 功能枚举
     */
    void asyncDispatch(ParamWrapper paramWrapper, String exteralNo, IPathEnum actionEnum, IFunctionEnum functionEnum) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
