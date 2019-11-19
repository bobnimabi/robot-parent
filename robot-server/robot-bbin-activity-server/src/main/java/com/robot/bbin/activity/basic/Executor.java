package com.robot.bbin.activity.basic;

import com.robot.center.execute.AbstractExecute;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.util.BoyerMoore;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 7:17 PM
 */
@Service
public class Executor extends AbstractExecute {

    @Override
    protected boolean isLose(StanderHttpResponse response) {
        return HttpStatus.SC_OK == response.getStatusLine().getStatusCode()
                && !StringUtils.isEmpty(response.getEntityStr())
                && (
                        -1 != BoyerMoore.find("ErrorCode: 130102025", response.getEntityStr())
                        || -1 != BoyerMoore.find("ErrorCode: 130102031", response.getEntityStr())
        );
    }

}
