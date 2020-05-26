//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.robot.center.tenant;

import com.bbin.common.constant.TenantConstant;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;

public class TContext {
    private static ThreadContext threadContext;

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

    public  void clean() {
        ThreadContext.clearMap();
    }

    private static String get(String name) {
        return ThreadContext.get(name);
    }

    private static void put(String name,String value) {
        ThreadContext.put(name, value);
    }
}
