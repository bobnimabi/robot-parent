package com.robot.core.task.execute;

import com.alibaba.fastjson.JSON;
import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.entity.TenantRobotHead;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.service.ITenantRobotDomainService;
import com.robot.code.service.ITenantRobotHeadService;
import com.robot.code.service.ITenantRobotPathService;
import com.robot.core.chain.Invoker;
import com.robot.core.function.base.IFunctionProperty;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.HttpMethodEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.protocol.HttpClientContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.util.List;

/**
 * 设置URL、Method、请求头、请求体、httpContext
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Slf4j
@Service
public class BeforeHttpProtocalChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {

    @Autowired
    private ITenantRobotDomainService domainService;

    @Autowired
    private ITenantRobotPathService pathService;

    @Autowired
    private ITenantRobotHeadService headService;


    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        // 请求URl和Method
        setUrlAndMethod(params, result);
        // 设置请求头
        setHeads(result);
        // 请求体
        result.setCustomEntity(params.getEntity());
        log.info("请求体：{}", JSON.toJSONString(params.getEntity()));
        // Cookie
        setHttpContext(params, result);

        invoker.invoke(params, result);
    }

    /**
     * 设置url和method
     * @param params
     * @param result
     */
    private void setUrlAndMethod(IFunctionProperty params, ExecuteProperty result) {
        TenantRobotPath path = pathService.getPath(params.getAction().getpathCode());
        TenantRobotDomain domain = domainService.getDomain(path.getRank());

        // 请求URl
        result.setUrl(URI.create(domain.getDomain() + path.getPath()));
        // 设置method
        result.setMethod(HttpMethodEnum.valueOf(path.getMethod()));
        log.info("请求Method:{},请求URL：{}", path.getMethod(), result.getUrl().toString());
    }

    /**
     * 设置请求头
     * @param result
     */
    private void setHeads(ExecuteProperty result) {
        List<TenantRobotHead> publicHeaders = headService.getPublicHeaders();
        CustomHeaders headers = result.getHeaders();
        for (TenantRobotHead head : publicHeaders) {
            Assert.hasText(head.getHeadName(), "执行前拦截：头信息:headName为空");
            Assert.hasText(head.getHeadValue(), "执行前拦截：头信息:headValue为空");
            headers.add(head.getHeadName(), head.getHeadValue());
        }
        if (!headers.isEmpty()) {
            log.info("请求头：{}", JSON.toJSON(headers.getHeaders()));
        }
    }

    /**
     * 设置HttpContext
     * @param params
     * @param result
     */
    private void setHttpContext(IFunctionProperty params, ExecuteProperty result) {
        HttpClientContext httpContext = HttpClientContext.create();
        httpContext.setCookieStore(params.getRobotWrapper().getCookieStore());
        result.setHttpContext(httpContext);
    }

    @Override
    public int order() {
        return 2;
    }
}
