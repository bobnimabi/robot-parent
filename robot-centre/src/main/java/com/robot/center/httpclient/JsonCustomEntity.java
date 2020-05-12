package com.robot.center.httpclient;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrt on 10/17/2019 6:58 PM
 * 调用HttpClientHelper#PostJson()方法的时候用
 * 注意：正常条件下，json的key是不允许重复的
 */
@Data
@Slf4j
public class JsonCustomEntity implements ICustomEntity<Object>{
    private Map<String,Object> entity = new HashMap<>(8);

    @Override
    public ICustomEntity add(String key, Object value) {
        if (StringUtils.isBlank(key)) {
            log.error("dictKey:{}，dictValue:{} 新增Entity：dictKey-dictValue 有空值,不予添加", key, value);
            return this;
        }
        entity.put(key, value);
        return this;
    }

    public static JsonCustomEntity custom() {
        return new JsonCustomEntity();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(entity);
    }
}
