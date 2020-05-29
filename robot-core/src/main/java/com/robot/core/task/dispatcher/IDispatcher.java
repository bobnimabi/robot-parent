package com.robot.core.task.dispatcher;//package com.bbin.robotWrapper.core.schedue;


import com.robot.code.dto.Response;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IFunctionEnum;
import com.robot.core.function.base.ParamWrapper;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by mrt on 2019/7/11 0011 下午 6:35
 */
public interface IDispatcher {

    /**
     * 轮询同步调用
     * @param paramWrapper 请求参数包装
     * @param functionEnum 功能枚举
     */
    Response dispatch(ParamWrapper paramWrapper, IFunctionEnum functionEnum) throws Exception;

    /**
     * 指定机器人同步调用
     * 1.登录相关接口使用
     * 2.指定特定机器人使用
     * @param paramWrapper
     * @param functionEnum
     * @param robotId
     * @param isNewCookie
     * @return
     */
    Response disPatcherSpec(ParamWrapper paramWrapper, IFunctionEnum functionEnum, long robotId, boolean isNewCookie) throws Exception;

    /**
     * 轮询异步分发
     *
     * @param paramWrapper 请求参数包装
     * @param exteralNo        外部订单号
     * @param actionEnum   等待path枚举
     * @param functionEnum 功能枚举
     */
    void asyncDispatch(ParamWrapper paramWrapper, String exteralNo, IPathEnum actionEnum, IFunctionEnum functionEnum) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
