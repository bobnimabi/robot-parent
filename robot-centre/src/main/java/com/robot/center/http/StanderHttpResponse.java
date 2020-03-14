package com.robot.center.http;

import com.bbin.common.response.ResponseResult;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.ResponseEntity;

/**
 * Created by mrt on 11/1/2019 6:46 PM
 */
@Data
public class StanderHttpResponse {
    // 状态行
    private StatusLine statusLine;
    // 响应头
    private Header[] headers;
    // 响应体（String）
    private String entityStr;
    // 转换后的响应体
    private ResponseResult responseResult;
    // 机器人订单号
    private String recordId;
}
