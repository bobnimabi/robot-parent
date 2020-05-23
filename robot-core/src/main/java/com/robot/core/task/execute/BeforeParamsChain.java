package com.robot.core.task.execute;

import com.robot.code.entity.HttpRequestConfig;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.service.IHttpRequestConfigService;
import com.robot.code.service.ITenantRobotPathService;
import com.robot.core.chain.Invoker;
import com.robot.core.common.CoreConsts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 接口请求相关参数：重定向、获取连接超时、请求超时、响应超时
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class BeforeParamsChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {
    @Autowired
    private ITenantRobotPathService pathService;

    @Autowired
    private IHttpRequestConfigService configService;

    /**
     * 最大重定向次数
     */
    private static final int MAX_REDIRECT = 10;

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        int rank = params.getRank();
        TenantRobotPath path = pathService.getPath(params.getAction().getActionCode(), rank);

        // requestConfigId
        Long requestConfigId = path.getHttpRequestConfigId();
        if (null != requestConfigId) {
            HttpRequestConfig config = configService.getConfigById(requestConfigId);

            // 设置重试
            HttpContext httpContext = result.getHttpContext();
            httpContext.setAttribute(CoreConsts.RETRY_FLAG, config.getIsRetry());

            // 设置请求配置参数
            RequestConfig.Builder requestConfig = result.getRequestConfig();
            requestConfig.setRedirectsEnabled(config.getIsRedirect());
            requestConfig.setRelativeRedirectsAllowed(config.getIsRedirect()); // 5.0版本取消了该属性
            requestConfig.setCircularRedirectsAllowed(false);
            requestConfig.setMaxRedirects(MAX_REDIRECT);
            requestConfig.setConnectionRequestTimeout(config.getConnectRequestTimeout());
            requestConfig.setConnectTimeout(config.getConnectTimeout());
            // 5.0版本将ResponseTimeOut加上，现在暂时暂时
        }

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }
}
