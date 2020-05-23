package com.robot.core.task.execute;

import com.robot.code.entity.TenantRobotDomain;
import com.robot.code.entity.TenantRobotPath;
import com.robot.code.service.IHttpRequestConfigService;
import com.robot.code.service.ITenantRobotDomainService;
import com.robot.code.service.ITenantRobotPathService;
import com.robot.core.chain.Invoker;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 设置URL
 * @Author mrt
 * @Date 2020/5/20 13:37
 * @Version 2.0
 */
@Service
public class BeforeHttpClientChain extends ExecuteBeforeFilter<IFunctionProperty, ExecuteProperty> {

    // 正常情况下，机器人不超过10个
    private static Map<Long, HttpClientWrapper> httpClientMap = new HashMap<>(10);

    @Override
    public void dofilter(IFunctionProperty params, ExecuteProperty result, Invoker<IFunctionProperty, ExecuteProperty> invoker) throws Exception {


        invoker.invoke(params, result);
    }

    @Override
    public int order() {
        return 1;
    }
    /**
     * httpclient的获取
     */
    private CloseableHttpClient getHttpClient(long robotId, String idCard) {
        Assert.hasText(idCard, "执行前拦截：idCard为空");
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
                    // 没有登录或项目重启或分布式其他机器才会走这一句
                    client = clientFactory.createHttpClient(robotId);
                    if (null != client) {
                        httpClientMap.put(robotId, client);
                    }
                } finally {
                    lock.unlock();
                }
            }
        } else {
            /**
             * 已登录过的情况
             * LOCAL_ID_CARD一定存在
             *      1.如果ROBOT: ID_CARD不存在，只能来自于登录前,比如图片验证码，不新建
             *      2.如果ROBOT: ID_CARD存在 --->不相等才创建，新建
             *          场景：登录（本机）或登录后（其他机器）
             */
            if (isVersionNotAgree(robotId, idCard)) {
                client = clientFactory.createHttpClient(robotId);
            }
        }
        return client;
    }

    private boolean idNeedCreate(String idCard, HttpClientWrapper hcw) {
        return null == hcw || null == hcw.getHttpClient() || StringUtils.isEmpty(hcw.getIdCard())|| !idCard.equalsIgnoreCase(hcw.getIdCard());
    }

    private boolean isVersionNotAgree(long robotId, String idCard) {
        String idCardKey = AbstractRobotCache.createCacheRobotIdCardKey(robotId);
        String localIdCard = robotVersion.get(idCardKey);
        // 只要线上不是空，都应该重置本地的ID_CARD
        if (!StringUtils.isEmpty(idCard)) {
            robotVersion.put(idCardKey, idCard);
        }
        // 只有不相等的时候才需要重建
        return !StringUtils.isEmpty(idCard) && !StringUtils.isEmpty(localIdCard) && !idCard.equals(localIdCard);
    }
}
