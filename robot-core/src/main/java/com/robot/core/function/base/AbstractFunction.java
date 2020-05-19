package com.robot.core.function.base;

import com.bbin.common.response.ResponseResult;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotCard;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.core.task.execute.IActionEnum;
import com.robot.core.task.execute.IExecute;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 接口级Function父类
 * @Author mrt
 * @Date 2020/5/19 13:01
 * @Version 2.0
 */
public abstract class AbstractFunction<T, E> implements IFunction<T, E> {
    /**
     * 域名等级：1
     */
    private static final int RANK_ONE = 1;
    /**
     * 域名等级：2
     */
    protected static final int RANK_TWO = 2;

    @Autowired
    private IExecute iExecute;

    @Override
    public Response<E> doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        ExecuteProperty property = new ExecuteProperty(getRank(), getAction(), getHeaders(robotWrapper)
                ,getEntity(paramWrapper),isCheckLost(),getResultParse());
        StanderHttpResponse<Object,E> standerHttpResponse = iExecute.request(property);
        return standerHttpResponse.getResponse();
    }

    /**
     * 获取域名等级
     *
     * @return
     */
    protected int getRank() {
        return RANK_ONE;
    }


    /**
     * 获取动作
     * @return
     */
    protected abstract IActionEnum getAction();

    /**
     * 获取接口特定请求头
     * 注意：可以对公共头进行覆盖（tenant_robot_header表配置）
     * 有些特定的登录的token会存在于cookie的属性里面
     * @param robotWrapper
     * @return
     */
    protected CustomHeaders getHeaders(RobotWrapper robotWrapper) {
        return null;
    }

    /**
     * 获取请求体
     * @return
     */
    protected abstract ICustomEntity getEntity(ParamWrapper<T> paramWrapper);

    /**
     * 是否检查掉线
     * @return
     */
    protected boolean isCheckLost() {
        return true;
    }

    /**
     * 获取ResultParse
     * @return
     */
    protected abstract IResultParse<E> getResultParse();
}
