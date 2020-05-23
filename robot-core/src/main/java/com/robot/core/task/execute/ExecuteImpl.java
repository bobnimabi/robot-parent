package com.robot.core.task.execute;

import com.alibaba.fastjson.JSON;
import com.robot.code.service.IRequestRecordService;
import com.robot.code.service.IResponseRecordService;
import com.robot.core.chain.Invoker;
import com.robot.core.function.base.Response;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * @Author mrt
 * @Date 2020/5/23 18:46
 * @Version 2.0
 * T原始响应数据类型
 * E转换后数据类型
 */
public class ExecuteImpl<T,E> implements IExecute<T,E>, InitializingBean {

    @Autowired
    private List<ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty>> beforeFilters;
    private Invoker<IFunctionProperty, ExecuteProperty> beforeInvoker;

    @Autowired
    private List<ExecuteAfterFilter<StanderHttpResponse,IFunctionProperty>> afterFilters;
    private Invoker<StanderHttpResponse,IFunctionProperty> afterInvoker;

    @Autowired
    private IRequestRecordService requestRecordService;

    @Autowired
    private IResponseRecordService responseRecordService;

    @Override
    public StanderHttpResponse<T,E>  request(IFunctionProperty property) throws Exception {
        ExecuteProperty executeProperty = new ExecuteProperty();
        // 前拦截执行
        beforeInvoker.invoke(property,executeProperty);
        // 执行前：日志
        long logId = addRequestLog(property);
        // 执行
        StanderHttpResponse<T, E> shr = HttpClientExector.execute(executeProperty);
        // 请求结果处理
        Response<E> response = property.getResultParse().parse2Obj(shr);
        shr.setResponse(response);
        // 执行后：日志
        responseLog(logId, response, shr);
        // 后拦截器执行
        afterInvoker.invoke(shr,property);
        return shr;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        beforeInvoker = Invoker.buildInvokerChain(beforeFilters);
        afterInvoker = Invoker.buildInvokerChain(afterFilters);
    }

    /**
     * 前日志
     * @param p
     * @return
     */
    private long addRequestLog(IFunctionProperty p) {
        return requestRecordService.addRequestRecord(p.getRobotWrapper().getId(),
                p.getAction().getActionCode(), p.getOutNo(), JSON.toJSONString(p.getEntity()));
    }

    /**
     * 后日志
     * @return
     */
    private void responseLog(long requestLogId,Response<E> response,StanderHttpResponse<T, E> shr) {
        requestRecordService.updateRequestRecord(requestLogId, response.isSuccess(), response.getMessage());
        if (response.isSuccess() && null != response.getObj()) {
            responseRecordService.addResponseRecord(requestLogId, shr.getOriginalEntity(), JSON.toJSONString(response.getObj()));
        }
    }
}
