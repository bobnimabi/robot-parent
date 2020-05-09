package com.robot.gpk.base.basic;

import com.alibaba.fastjson.JSON;
import com.robot.center.execute.AbstractExecute;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.code.service.ITenantRobotDropService;
import com.robot.gpk.base.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 7:17 PM
 */
@Slf4j
@Service
public class Executor extends AbstractExecute {

    @Autowired
    private ITenantRobotDropService robotDropService;
    private static final String HEAD_NAME = "Location";



    // 是否掉线

    /**
     * 掉线的时候：
     * 状态码：401
     * 头：Location：/Account/Login?ReturnUrl=%2fMember%2fGetDetail
     */
    @Override
    protected boolean isLose(StanderHttpResponse response) {
        String entityStr = response.getEntityStr();
        String headValue = null;
        for (Header header : response.getHeaders()) {
            if (HEAD_NAME.equals(header.getName())) {
                headValue = header.getValue();
            }
        }
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED
                && null != headValue && headValue.contains("Login")) {
            return true;
        }
        return false;
    }
}
