package com.robot.core.http.schema;

import com.bbin.common.response.ResponseResult;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.StatusLine;

/**
 * Created by mrt on 11/1/2019 6:46 PM
 */
@Data
public class StanderHttpResponse {
    // 状态行
    private StatusLine statusLine;
    // 响应头
    private Header[] headers;
    // 二进制响应体
    private byte[] entity;
    // 响应体（String）
    private String entityStr;
    // 转换后的响应体
    private ResponseResult responseResult;
    private String recordId;
}
