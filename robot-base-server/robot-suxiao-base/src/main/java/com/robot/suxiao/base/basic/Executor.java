package com.robot.suxiao.base.basic;

import com.robot.center.execute.AbstractExecute;
import com.robot.center.http.StanderHttpResponse;
import com.robot.code.service.ITenantRobotDropService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    /**
     * 判断是否需掉线
     * @param response 响应结果
     * @return
     */
    @Override
    protected boolean isLose(StanderHttpResponse response) {
        String entityStr = response.getEntityStr();
        if (StringUtils.isEmpty(entityStr)) {
            return false;
        }
        Document doc = Jsoup.parse(entityStr);
        Element error = doc.getElementById("error");
        return null != error && !StringUtils.isEmpty(error.val());
    }
}
