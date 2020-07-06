package com.robot.core.function.base;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.robot.code.response.Response;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.CustomResponseHandler;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.core.task.execute.IExecute;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;

/**
 * 接口级Function父类
 *
 * @Author mrt
 * @Date 2020/5/19 13:01
 * @Version 2.0
 */
@Slf4j
public abstract class AbstractFunction<T, F, E> implements IFunction<T, F, E> {

    @Autowired
    private IExecute iExecute;

    /**
     * 注意，掉线检查服务的名称：CheckLostImpl
     * 1.正常情况下，一个机器人只需要一个掉线检查
     * 2.不排除有多个掉线检查的情况，如果有其他的function就覆盖getCHECK_LOST_SERVICE()方法
     */
    @Resource(name = "checkLostImpl")
    private ICheckLost CHECK_LOST_SERVICE;


    @Override
    public Response<E> doFunction(ParamWrapper<T> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        IPathEnum pathEnum = getPathEnum();
        log.info("-----------------{}-----------------", pathEnum.getPathCode());
        FunctionProperty property = new FunctionProperty(
                pathEnum,
                getHeaders(robotWrapper),
                getEntity(paramWrapper.getObj(), robotWrapper),
                getUrlSuffix(paramWrapper.getObj()),
                getCHECK_LOST_SERVICE(),
                getResponseHandler(),
                getResultHandler(),
                robotWrapper,
                getExteralNo(paramWrapper.getObj()),
                IdWorker.getId()
        );
        StanderHttpResponse<F, E> standerHttpResponse = iExecute.request(property);
        return standerHttpResponse.getResponse();
    }

    /**
     * 获取动作
     * @return
     */
    protected abstract IPathEnum getPathEnum();

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
    protected abstract IEntity getEntity(T params, RobotWrapper robotWrapper);


    /**
     * 获取URL后缀，默认：""
     * @return
     */
    protected String getUrlSuffix(T params) {
        return "";
    }

    /**
     * 是否检查掉线
     * 1.如果该接口不用公共掉线检查，通过@Override
     * 2.如果返回null，则不进行掉线检查（与登录相关接口）
     * @return
     */
    protected ICheckLost getCHECK_LOST_SERVICE() {
        return CHECK_LOST_SERVICE;
    }

    /**
     * 获取外部订单号，异步任务使用，通过@Override
     * @return
     */
    protected String getExteralNo(T params) {
        return null;
    }

    /**
     * 获取ResultParse
     * @return
     */
    protected abstract IResultHandler<F, E> getResultHandler();

    /**
     * 获取http响应处理器：流转文本或byte[]
     * 默认使用通用处理器
     * 注意：如果有错误状态码有特殊情况，通过自定义ResponseHandler，通过@Override
     * @return
     */
    protected ResponseHandler<StanderHttpResponse> getResponseHandler(){
        return CustomResponseHandler.RESPONSE_HANDLER;
    }
}
