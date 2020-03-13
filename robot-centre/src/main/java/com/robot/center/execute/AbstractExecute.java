package com.robot.center.execute;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bbin.common.response.ResponseResult;
import com.robot.center.constant.RobotConsts;
import com.robot.center.httpclient.*;
import com.robot.center.pool.AbstractRobotCache;
import com.robot.center.pool.IRobotManager;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.code.service.ITenantRobotRecordService;
import com.robot.code.service.ITenantRobotRespLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.ProviderMismatchException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mrt on 10/27/2019 6:40 PM
 */
@Slf4j
public abstract class AbstractExecute implements IExecute{
    @Autowired
    private AbstractHttpClientFactory clientFactory;
    @Autowired
    private ITenantRobotRespLogService respLogService;
    @Autowired
    private ITenantRobotRecordService recordService;
    @Autowired
    private IRobotManager robotManager;

    private Map<Long, CloseableHttpClient> httpClientMap = new HashMap<>();
    // 本地机器人版本
    private Map<String, String> robotVersion = new HashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public StanderHttpResponse request(RobotWrapper robotWrapper, CustomHttpMethod method, TenantRobotAction action, String externalOrderNo,
                                       ICustomEntity customEntity, CustomHeaders headers, IResultParse resultParse) {
        return this.request(robotWrapper, method, action, externalOrderNo,
                customEntity, headers, resultParse,false);
    }

    /**
     * 请求
     * @param robotWrapper 机器人包装类
     * @param method 请求方法
     * @param action 动作
     * @param externalOrderNo 外部订单号
     * @param customEntity 请求体
     * @param headers 请求头
     * @param resultParse 响应结果转换
     * @return
     * 返回null的情况
     * 1.请求无响应
     * 2.请求过程出现异常
     */
    public StanderHttpResponse request(RobotWrapper robotWrapper, CustomHttpMethod method, TenantRobotAction action, String externalOrderNo,
                                       ICustomEntity customEntity, CustomHeaders headers, IResultParse resultParse,boolean checkLose) {
        // 获取httpClient
        CloseableHttpClient httpClient = getHttpClient(robotWrapper.getId(),robotWrapper.getIdCard());
        // 获取请求路径
        String url = action.getActionUrl();
        // 生成流水id
        String idStr = IdWorker.getIdStr();
        // 请求前：校验、日志
        beforeProcess(idStr,robotWrapper,httpClient,url,externalOrderNo,action.getActionCode(),
                customEntity, method, resultParse);
        // 请求
        HttpClientContext httpContext = HttpClientContext.create();
        if (null != robotWrapper) {
            httpContext.setCookieStore(robotWrapper.getCookieStore());
            httpContext.setAttribute(RobotConsts.ROBOT_ID, robotWrapper.getId());
        }
        StanderHttpResponse standerHttpResponse = requestDetail(httpClient, url, customEntity, headers, method, httpContext);
        standerHttpResponse.setRecordId(idStr);
        // 请求后：日志、响应解析
        return afterProcess(standerHttpResponse, idStr, robotWrapper, resultParse, checkLose);
    }

    /**
     * 判断是否需掉线
     * @param response 响应结果
     * @return
     */
    protected abstract boolean isLose(StanderHttpResponse response);

    private <T>void beforeProcess(String idStr,RobotWrapper robotWrapper,CloseableHttpClient httpClient, String url,String externalOrderNo,
                                  String actionCode, ICustomEntity customEntity, CustomHttpMethod method, IResultParse resultParse) {
        log.info("RecordId:{} RobotId:{} \r\n Method:{} Url:{} ExternalOrderNo:{}",
                idStr,robotWrapper.getId(),method.name(),url,externalOrderNo);
        log.info("请求体参数：{}", customEntity == null ? "" : customEntity.toString());
        // 请求前：校验参数合法性
        validate(httpClient, url, method, resultParse);
        // 请求前：日志记录
        recordService.addRecordAsync(idStr, robotWrapper.getId(), externalOrderNo, actionCode, RobotConsts.RobotRecord.SENDING, JSON.toJSONString(customEntity));
    }

    /**
     * 请求后处理
     *
     * @param standarHttpResponse
     * @param idStr
     * @param robotWrapper
     * @param resultParse
     * @return
     */
    private StanderHttpResponse afterProcess(StanderHttpResponse standarHttpResponse, String idStr, RobotWrapper robotWrapper,
                                             IResultParse resultParse, boolean checkLose) {
        // 响应文本：存档
        respLogService.saveRespRecordAsync(idStr, JSON.toJSONString(standarHttpResponse));
        // 检查是否响应为login标志
        String entityStr = standarHttpResponse.getEntityStr();
        if (checkLose && isLose(standarHttpResponse)) {
            robotManager.closeRobot(robotWrapper.getId());
            standarHttpResponse.setResponseResult(ResponseResult.LOSE("机器人掉线"));
        } else if (StringUtils.hasText(entityStr)) {
            // 请求结果转对象
            ResponseResult parse = resultParse.parse(entityStr);
            standarHttpResponse.setResponseResult(parse);
            if (!(null != parse.getObj() && parse.getObj() instanceof String)) {
                log.info("响应结果(已转换)：" + JSON.toJSONString(parse));
            }
        } else {
            log.error("响应结果：未响应Entity:{}", JSON.toJSONString(standarHttpResponse));
            standarHttpResponse.setResponseResult(ResponseResult.FAIL("未响应Entity..."));
        }
        // 请求后：日志记录
        String respJson = null != standarHttpResponse.getResponseResult() ? JSON.toJSONString(standarHttpResponse.getResponseResult()) : "";
        recordService.updateRecordAsync(idStr, respJson, RobotConsts.RobotRecord.SENDING);
        return standarHttpResponse;
    }

    /**
     *
      * @param httpClient 请求客户端
     * @param url
     * @param customEntity 请求体
     * @param headers 请求有
     * @param method 请求方法
     * @param httpContext
     * @return
     * 返回null的情况
     * 1.请求过程出现异常
     * 2.请求无响应（响应的Entity为空）
     */
    private StanderHttpResponse requestDetail(CloseableHttpClient httpClient, String url,
                                 ICustomEntity customEntity, CustomHeaders headers, CustomHttpMethod method, HttpClientContext httpContext) {
        StanderHttpResponse result = null;
        result = null;
        try {
            switch (method) {
                case GET:
                    result = HttpClientHelper.get(httpClient, url, (UrlCustomEntity) customEntity, headers, httpContext);
                    break;
                case POST_FORM:
                    result = HttpClientHelper.postForm(httpClient, url, (UrlCustomEntity) customEntity, headers, httpContext);
                    break;
                case POST_JSON:
                    result = HttpClientHelper.postJson(httpClient, url, (JsonCustomEntity) customEntity, headers, httpContext);
                    break;
                default:
                    throw new ProviderMismatchException("请求方式错误:" + url);
            }
        } catch (UnknownHostException e) {
            log.error("请求异常：域名解析问题（服务器网络中断）", e);
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e1) {
            }
        } catch (URISyntaxException e) {
            log.error("请求异常：URL语法错误", e);
        } catch (IOException e) {
            log.error("请求异常：流读写错误", e);
        } catch (Exception e) {
            log.error("请求异常", e);
        }
        return result;
    }

    /**
     * 校验
     * @param httpClient
     * @param url
     */
    private void validate(CloseableHttpClient httpClient, String url, CustomHttpMethod method, IResultParse resultParse) {
        Assert.notNull(httpClient, "HttpClient为null");
        Assert.hasText(url, "URL为空");
        Assert.notNull(resultParse, "ResultParse为null");
    }

    /**
     * httpclient的获取
     * 1.保证机器人新登录后，分布式下所有的httpclient要全部重置
     */
    private CloseableHttpClient getHttpClient(long robotId,String idCard) {
        CloseableHttpClient client = httpClientMap.get(robotId);
        // 首次使用Httpclient，或项目重启
        if (null == client) {
            if (lock.tryLock()) {
                try {
                    if (null == client) {
                        // 没有登录或项目重启或分布式其他机器才会走这一句
                        client = clientFactory.createHttpClient(robotId);
                        if (null != client) {
                            httpClientMap.put(robotId, client);
                        }
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
