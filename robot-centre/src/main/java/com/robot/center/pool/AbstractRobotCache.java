package com.robot.center.pool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.robot.center.constant.RobotConsts;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.service.impl.TenantRobotServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by mrt on 2019/7/5 0005 下午 11:49
 * 机器人管理池
 * TODO 有一种场景：表里的机器人存活，但是缓存机器人和标志都没有了
 *      1.redis突然挂掉
 *      2.获取机器人后没归还回来（项目突然挂了）
 */
@Slf4j
public abstract class AbstractRobotCache extends TenantRobotServiceImpl implements IRobotCache{
    // 类名称
    protected static String POOL_CLASS_NAME = "机器人管理:";
    // 机器人池缓存前缀
    private static final String CACHE_ROBOT_POOL_PREFIX = RobotConsts.ROBOT_PROJECT_PERFIX + "POOL:QUEUE:";
    // 机器人标志缓存前缀
    private static final String CACHE_ROBOT_ID_CARD_PREFIX = RobotConsts.ROBOT_PROJECT_PERFIX + "POOL:ID_CARD:";

    @Autowired
    protected StringRedisTemplate stringRedis;

    /**
     * 获取机器人
     * 返回null的情况
     * 1.队列里面没有机器人
     * 2.机器人过期（已被删除）
     */
    @Override
    public RobotWrapper cacheRobotGet() {
        String robotJson = stringRedis.opsForList().rightPop(createCacheRobotPoolKey());
        RobotWrapper robotWrapper = deserializationRobot(robotJson);
        if (!isLegal(robotWrapper)) {
            return null;
        }
        log.info("获取机器人成功：robotId:{}", robotWrapper.getId());
        return robotWrapper;
    }

    @Override
    public void cacheGiveBack(RobotWrapper robot) {
        if (isLegal(robot)) {
            Long aLong = stringRedis.opsForList().leftPush(createCacheRobotPoolKey(), serializationRobot(robot));
            if (aLong > 0) {
                log.info("{}：归还机器人成功,robotId:{}",POOL_CLASS_NAME,robot.getId());
            } else {
                log.info("{}：归还机器人失败，将强制关闭机器人;{}",POOL_CLASS_NAME,JSON.toJSONString(robot));
                closeRobot(robot.getId());
            }
        }
    }

    protected boolean cacheRobotAdd(RobotWrapper robot) {
        if (null == robot || null == robot.getCookieStore() || null == robot.getId()) {
            log.info("{}:新增机器人不合法：将不添加：{}", POOL_CLASS_NAME, JSON.toJSONString(robot));
            return false;
        }
        // 更新标志
        stringRedis.opsForValue().set(createCacheRobotIdCardKey(robot.getId()), robot.getIdCard());
        // 入队
        Long aLong = stringRedis.opsForList().leftPush(createCacheRobotPoolKey(), serializationRobot(robot));
        return aLong > 0;
    }

    @Override
    protected void forcedOfflineByCache(long robotId) {
        // 缓存：丢弃机器人标志
        Boolean idDiscard = stringRedis.delete(createCacheRobotIdCardKey(robotId));
        if (!idDiscard) {
            Boolean hasKey = stringRedis.hasKey(createCacheRobotIdCardKey(robotId));
            if (hasKey) {
                throw new IllegalStateException("CACHE:强制下线机器人失败,robotId:" + robotId);
            }
        }
    }
    // 校验机器人存在合法性
    private boolean isLegal(RobotWrapper robotWrapper) {
        if (null == robotWrapper) {
            return false;
        }
        if (null == robotWrapper.getCookieStore() || null == robotWrapper.getId() || StringUtils.isEmpty(robotWrapper.getIdCard())) {
            log.info("{}:校验机器人合法性：参数不全，将销毁：{}", POOL_CLASS_NAME, JSON.toJSONString(robotWrapper));
            return false;
        }
        String idCard = stringRedis.opsForValue().get(createCacheRobotIdCardKey(robotWrapper.getId()));
        boolean isLegal = !StringUtils.isEmpty(idCard) && idCard.equals(robotWrapper.getIdCard());
        if (!isLegal) {
            log.info("{}:校验机器人合法性：ID_CARD不合法，将销毁：{}", POOL_CLASS_NAME, JSON.toJSONString(robotWrapper));
        }
        return isLegal;
    }

    // 创建机器人池的缓存key
    private static String createCacheRobotPoolKey() {
        return new StringBuilder(35)
                .append(CACHE_ROBOT_POOL_PREFIX)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction())
                .toString();
    }

    // 创建机器人标志的缓存key
    private static String createCacheRobotIdCardKey(long robotId) {
        return new StringBuilder(35)
                .append(CACHE_ROBOT_ID_CARD_PREFIX)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(robotId)
                .toString();
    }

    private static String serializationRobot(RobotWrapper robot) {
        return JSON.toJSONString(robot);
    }

    private static RobotWrapper deserializationRobot(String robotJson) {
        if (StringUtils.isEmpty(robotJson)) {
            return null;
        }
        RobotWrapper robotWrapper = JSON.parseObject(robotJson, RobotWrapper.class);

        JSONArray cookies = JSON.parseObject(robotJson).getJSONObject("cookieStore").getJSONArray("cookies");
        CookieStore cookieStoreNew = new BasicCookieStore();
        for (int i = 0; i < cookies.size(); i++) {
            JSONObject jsonObject = cookies.getJSONObject(i);
            String name = jsonObject.getString("name");
            String value = jsonObject.getString("value");
            String domain = jsonObject.getString("domain");
            String path = jsonObject.getString("path");
            Boolean secure = jsonObject.getBoolean("secure");
            String expiryDate = jsonObject.getString("expiryDate");
            String creationDate = jsonObject.getString("creationDate");
            Integer version = jsonObject.getInteger("version");

            BasicClientCookie cookie = new BasicClientCookie(name, value);
            cookie.setDomain(domain);
            cookie.setPath(path);
            if (null != expiryDate) {
                cookie.setExpiryDate(new Date(Long.parseLong(expiryDate)));
            }
            if (null != creationDate) {
                cookie.setCreationDate(new Date(Long.parseLong(creationDate)));
            }
            if (null != secure) {
                cookie.setSecure(secure);
            }
            if (null != version) {
                cookie.setVersion(version);
            }

            cookieStoreNew.addCookie(cookie);
        }
        robotWrapper.setCookieStore(cookieStoreNew);
        return robotWrapper;
    }
}

