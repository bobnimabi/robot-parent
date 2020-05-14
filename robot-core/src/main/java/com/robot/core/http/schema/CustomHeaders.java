package com.robot.core.http.schema;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 10/17/2019 6:58 PM
 */
@Data
@Slf4j
public class CustomHeaders {
    private List<Header> headers = new ArrayList<>(3);

    public CustomHeaders add(String headKey, String headValue) {
        if (StringUtils.isBlank(headKey)) {
            log.error("headKey:{}，headValue:{} 新增Heads：dictKey-dictValue 有空值,不予添加", headKey, headValue);
            return this;
        }
        headers.add(new BasicHeader(headKey, headValue));
        return this;
    }

    public static CustomHeaders build() {
        return new CustomHeaders();
    }
}
