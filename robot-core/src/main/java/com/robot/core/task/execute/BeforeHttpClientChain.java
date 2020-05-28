package com.robot.core.task.execute;

import com.robot.core.chain.Invoker;
import com.robot.core.function.base.IFunctionProperty;
import com.robot.core.httpclient.factory.IHttpClientFactory;
import com.robot.core.robot.manager.RobotWrapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
public class BeforeHttpClientChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {

    @Autowired
    private IHttpClientFactory httpClientFactory;

    // 正常情况下，机器人不超过10个
    private static Map<Long, HttpClientWrapper> httpClientMap = new HashMap<>(10);

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {
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
     */
    private CloseableHttpClient getHttpClient(long robotId, String idCard) throws Exception {
        HttpClientWrapper hcw = httpClientMap.get(robotId);

        // 首次使用Httpclient，或项目重启
        if (this.idNeedCreate(idCard, hcw)) {
            ReentrantLock lock = new ReentrantLock();
            if (lock.tryLock()) {
                try {
                    hcw = httpClientMap.get(robotId);
                    if (!this.idNeedCreate(idCard, hcw)) {
                        return hcw.getHttpClient();
                    }
                    CloseableHttpClient httpClient = httpClientFactory.create();
                    httpClientMap.put(robotId, new HttpClientWrapper(httpClient, idCard));
                    return httpClient;
                } finally {
                    lock.unlock();
                }
            }
        }
        return hcw.getHttpClient();
    }

    private boolean idNeedCreate(String idCard, HttpClientWrapper hcw) {
        return null == hcw || null == hcw.getHttpClient() || StringUtils.isEmpty(hcw.getIdCard()) || StringUtils.isEmpty(idCard) || !idCard.equalsIgnoreCase(hcw.getIdCard());
    }

}
