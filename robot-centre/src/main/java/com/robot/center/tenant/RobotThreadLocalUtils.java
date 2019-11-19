//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.robot.center.tenant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RobotThreadLocalUtils {
    private static InheritableThreadLocal<Map<String, Long>> threadLocal = new InheritableThreadLocal();
    public static String OPERATOR_ID = "operator_id";
    public static String TENANT_ID = "tenant_id";
    public static String CHANNEL_ID = "channel_id";
    public static String PLATFORM_ID = "platform_id";
    public static String FUNCTION = "function";

    public static Long getOperatorId() {
        return Optional.of(get(OPERATOR_ID)).get();
    }

    public static Long getTenantId() {
        return Optional.of(get(TENANT_ID)).get();
    }

    public static Long getChannelId() {
        return Optional.of(get(CHANNEL_ID)).get();
    }

    public static Long getPlatformId() {
        return Optional.of(get(PLATFORM_ID)).get();
    }

    public static Long getFunction() {
        return Optional.of(get(FUNCTION)).get();
    }

    public static void setOperatorId(Long operatorId) {
        set(OPERATOR_ID, operatorId);
    }

    public static void setTenantId(Long tenantId) {
        set(TENANT_ID, tenantId);
    }

    public static void setChannelId(Long channelId) {
        set(CHANNEL_ID, channelId);
    }

    public static void setPlatformId(Long platformId) {
        set(PLATFORM_ID, platformId);
    }

    public static void setFunction(Long function) {
        set(FUNCTION, function);
    }

    public static void clean() {
        threadLocal.remove();
    }

    public static void set(String key, Long value) {
        Optional.ofNullable(value).orElseThrow(() -> {
            return new IllegalArgumentException("参数key 为空");
        });
        Optional.ofNullable(key).orElseThrow(() -> {
            return new IllegalArgumentException("参数value 为空");
        });
        Map<String, Long> cache = (Map)Optional.ofNullable(threadLocal.get()).orElseGet(() -> {
            Map<String, Long> map = new HashMap();
            threadLocal.set(map);
            return map;
        });
        cache.put(key, value);
    }

    private static Long get(String name) {
        Map<String, Long> cache = (Map)threadLocal.get();
        return cache != null ? (Long)cache.get(name) : null;
    }
}
