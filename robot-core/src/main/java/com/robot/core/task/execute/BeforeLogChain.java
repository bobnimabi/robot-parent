package com.robot.core.task.execute;

import com.alibaba.fastjson.JSON;
import com.robot.code.entity.HttpRequestConfig;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.entity.TenantRobotProxy;
import com.robot.code.service.IHttpRequestConfigService;
import com.robot.code.service.IRequestRecordService;
import com.robot.code.service.ITenantRobotPathService;
import com.robot.code.service.ITenantRobotProxyService;
import com.robot.core.chain.Invoker;
import com.robot.core.common.CoreConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 请求日志记录
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Slf4j
@Service
public class BeforeLogChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {
    @Autowired
    private IRequestRecordService requestRecordService;

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
        requestRecordService.addRequestRecord(
                params.getRecordId(),
                params.getRobotWrapper().getId(),
                params.getAction().getActionCode(),
                params.getOutNo(),
                JSON.toJSONString(params.getEntity()));

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 4;
    }
}
