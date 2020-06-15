package com.robot.core.http.request;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 10/17/2019 6:58 PM
 * 调用
 * HttpClientHelper#get()
 * HttpClientHelper#postForm()
 * HttpClientHelper#uploadFile()
 * 方法的时候用
 * 注意：url的key是可以重复的
 */
@Data
@Slf4j
public class UrlEntity implements IEntity<String, String> {

    private List<NameValuePair> entity;

    private UrlEntity(int size) {
        entity = new ArrayList<>(size);
    }

    @Override
    public UrlEntity add(String key, String value) {
        if (StringUtils.isBlank(key)) {
            log.error("dictKey:{}，dictValue:{} 新增Entity：dictKey-dictValue 有空值,不予添加", key, value);
            return this;
        }
        entity.add(new BasicNameValuePair(key, value));
        return this;
    }

    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(entity);
    }

    public static UrlEntity custom(int size) {
        return new UrlEntity(size);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(entity);
    }
}
