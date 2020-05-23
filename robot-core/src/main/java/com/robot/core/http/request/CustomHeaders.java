package com.robot.core.http.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 10/17/2019 6:58 PM
 */
@Data
@Slf4j
public class CustomHeaders implements ICustomEntity<String,String> {
    private List<Header> headers;

    private CustomHeaders(int size) {
        headers = new ArrayList<>(size);
    }

    @Override
    public CustomHeaders add(String headKey, String headValue) {
        if (StringUtils.isBlank(headKey)) {
            log.error("headKey:{}，headValue:{} 新增Heads：dictKey-dictValue 有空值,不予添加", headKey, headValue);
            return this;
        }
        headers.add(new BasicHeader(headKey, headValue));
        return this;
    }

    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(headers);
    }

    public static CustomHeaders custom(int size) {
        return new CustomHeaders(size);
    }
}
