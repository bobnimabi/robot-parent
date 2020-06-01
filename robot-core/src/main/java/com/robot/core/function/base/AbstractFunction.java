package com.robot.core.function.base;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.robot.code.dto.Response;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.core.task.execute.IExecute;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * 接口级Function父类
 *
 * @Author mrt
 * @Date 2020/5/19 13:01
 * @Version 2.0
 */
public abstract class AbstractFunction<T, F, E> implements IFunction<T, F, E> {

    @Autowired
    private IExecute iExecute;

    @Resource(name = "checkLostImpl")
    private ICheckLost CHECK_LOST_SERVICE;

    @Override
    public final Response<E> doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        IFunctionProperty property = new FunctionProperty(getPathEnum(), getHeaders(robotWrapper)
                , getEntity(paramWrapper.getObj()), getCHECK_LOST_SERVICE(), getResultHandler(), robotWrapper, getExteralNo(paramWrapper), IdWorker.getId());
        StanderHttpResponse<F, E> standerHttpResponse = iExecute.request(property);
        return standerHttpResponse.getResponse();
    }


    /**
     * 获取动作
     *
     * @return
     */
    protected abstract IPathEnum getPathEnum();

    /**
     * 获取接口特定请求头
     * 注意：可以对公共头进行覆盖（tenant_robot_header表配置）
     * 有些特定的登录的token会存在于cookie的属性里面
     *
     * @param robotWrapper
     * @return
     */
    protected CustomHeaders getHeaders(RobotWrapper robotWrapper) {
        return null;
    }

    /**
     * 获取请求体
     *
     * @return
     */
    protected abstract ICustomEntity getEntity(T params);

    /**
     * 是否检查掉线
     *
     * @return
     */
    protected ICheckLost getCHECK_LOST_SERVICE() {
        return CHECK_LOST_SERVICE;
    }

    /**
     * 获取外部订单号
     * @return
     */
    protected String getExteralNo(ParamWrapper<T> paramWrapper) {
        return null;
    }

    /**
     * 获取ResultParse
     *
     * @return
     */
    protected abstract IResultHandler<F, E> getResultHandler();
}
