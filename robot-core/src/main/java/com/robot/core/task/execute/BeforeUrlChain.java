package com.robot.core.task.execute;

import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.service.IHttpRequestConfigService;
import com.robot.code.service.ITenantRobotDomainService;
import com.robot.code.service.ITenantRobotPathService;
import com.robot.core.chain.Invoker;
import com.robot.core.http.request.HttpMethodEnum;
import org.apache.http.client.protocol.HttpClientContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * 设置URL
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class BeforeUrlChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {

    @Autowired
    private ITenantRobotDomainService domainService;

    @Autowired
    private ITenantRobotPathService pathService;

    @Autowired
    private IHttpRequestConfigService configService;

    private static final int MAX_REDIRECT = 10;

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        int rank = params.getRank();
        TenantRobotDomain domain = domainService.getDomain(rank);
        TenantRobotPath path = pathService.getPath(params.getAction().getActionCode(), rank);

        // 请求URl
        result.setUrl(URI.create(domain.getDomain() + path.getPath()));
        // 请求Method
        result.setMethod(HttpMethodEnum.valueOf(path.getMethod()));
        // 请求体
        result.setCustomEntity(params.getEntity());
        // Cookie
        HttpClientContext httpContext = HttpClientContext.create();
        httpContext.setCookieStore(params.getRobotWrapper().getCookieStore());
        result.setHttpContext(httpContext);

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }
}
