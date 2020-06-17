package com.robot.core.task.execute;

import com.robot.core.chain.Invoker;
import com.robot.core.function.base.FunctionProperty;
import com.robot.core.httpclient.factory.IHttpClientFactory;
import com.robot.core.robot.manager.RobotWrapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 设置HttpClient
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class BeforeHttpClientChain extends ExecuteBeforeFilter<FunctionProperty, ExecuteProperty> {

    @Autowired
    private IHttpClientFactory httpClientFactory;

    /**
     * httpClient容器
     */
    private static Map<Long, HttpClientWrapper> httpClientMap = new HashMap<>(12);

    @Override
    public void dofilter(FunctionProperty params, ExecuteProperty result, Invoker<FunctionProperty, ExecuteProperty> invoker) throws Exception {
        RobotWrapper robotWrapper = params.getRobotWrapper();
        CloseableHttpClient httpClient = getHttpClient(robotWrapper.getId(), robotWrapper.getIdCard());
        result.setHttpClient(httpClient);

        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }
    /**
     * httpclient的获取
     * 需要创建的情况
     * 1.首次使用时（项目重启）
     * 2.机器人重新登录以后
     */
    private CloseableHttpClient getHttpClient(long robotId, String idCard) throws Exception {
        HttpClientWrapper hcw = httpClientMap.get(robotId);
        // 首次使用Httpclient，或项目重启
        if (this.idNeedCreate(idCard, hcw)) {
            CloseableHttpClient httpClient = httpClientFactory.create();
            hcw = new HttpClientWrapper(httpClient, idCard);
            httpClientMap.put(robotId, hcw);
        }
        return hcw.getHttpClient();
    }

    private boolean idNeedCreate(String idCard, HttpClientWrapper hcw) {
        return null == hcw || null == hcw.getHttpClient() || StringUtils.isEmpty(hcw.getIdCard()) || StringUtils.isEmpty(idCard) || !idCard.equalsIgnoreCase(hcw.getIdCard());
    }
}
