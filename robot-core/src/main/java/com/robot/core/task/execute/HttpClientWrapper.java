package com.robot.core.task.execute;

import com.robot.core.robot.manager.RobotWrapper;
import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @Author mrt
 * @Date 2020/5/23 15:40
 * @Version 2.0
 */
@Data
public class HttpClientWrapper {
    /**
     * httpclient
     */
    private CloseableHttpClient httpClient;
    /**
     * 身份标识，全局唯一
     */
    private String idCard;

}
