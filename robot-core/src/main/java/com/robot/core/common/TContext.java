//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.robot.core.common;

import com.bbin.common.constant.TenantConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.util.Assert;
@Slf4j
public class TContext {

    public static String PLATFORM_ID = "platform_id";
    public static String FUNCTION = "function";


    public static String getTenantId() {
        return ThreadContext.get(TenantConstant.TENANT_ID);
    }

    public static String getChannelId() {
        return ThreadContext.get(TenantConstant.CHANNEL_ID);
    }

    public static String getPlatformId() {
        return ThreadContext.get(PLATFORM_ID);
    }

    public static String getFunction() {
        return ThreadContext.get(FUNCTION);
    }


    public static void setTenantId(String tenantId) {
        Assert.hasText(tenantId, "tenantId为空");
        ThreadContext.put(TenantConstant.TENANT_ID, tenantId);
    }

    public static void setChannelId(String channelId) {
        Assert.hasText(channelId, "channelId为空");
        ThreadContext.put(TenantConstant.CHANNEL_ID, channelId);
    }

    public static void setPlatformId(String platformId) {
        Assert.hasText(platformId, "platformId为空");
        ThreadContext.put(PLATFORM_ID, platformId);
    }

    public static void setFunction(String function) {
        Assert.hasText(function, "function为空");
        ThreadContext.put(FUNCTION, function);
    }

    public static void clean() {
        try {
            ThreadContext.clearMap();
        } catch (Exception e) {
            log.error("ThreadContext：清理租户信息异常", e);
        }
    }

    private static String get(String name) {
        return ThreadContext.get(name);
    }

    private static void put(String name,String value) {
        ThreadContext.put(name, value);
    }
}
