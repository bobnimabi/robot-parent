package com.robot.core.http.protocal;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
public class UrlCustomEntity implements ICustomEntity<String>{
    private List<NameValuePair> entity = new ArrayList<>(8);

    @Override
    public UrlCustomEntity add(String key, String value) {
        if (StringUtils.isBlank(key)) {
            log.error("dictKey:{}，dictValue:{} 新增Entity：dictKey-dictValue 有空值,不予添加", key, value);
            return this;
        }
        entity.add(new BasicNameValuePair(key, value));
        return this;
    }

    public static UrlCustomEntity custom() {
        return new UrlCustomEntity();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(entity);
    }
}
