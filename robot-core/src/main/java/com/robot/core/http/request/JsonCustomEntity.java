package com.robot.core.http.request;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrt on 10/17/2019 6:58 PM
 * 调用HttpClientHelper#PostJson()方法的时候用
 * 注意：正常条件下，json的key是不允许重复的
 */
@Data
@Slf4j
public class JsonCustomEntity implements ICustomEntity<String, Object> {

    private Map<String, Object> entity;

    private JsonCustomEntity(int size) {
        entity = new HashMap<String, Object>(size);
    }

    @Override
    public ICustomEntity add(String key, Object value) {
        if (StringUtils.isBlank(key)) {
            log.error("dictKey:{}，dictValue:{} 新增Entity：dictKey-dictValue 有空值,不予添加", key, value);
            return this;
        }
        entity.put(key, value);
        return this;
    }

    public static JsonCustomEntity custom(int size) {
        return new JsonCustomEntity(size);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(entity);
    }
}