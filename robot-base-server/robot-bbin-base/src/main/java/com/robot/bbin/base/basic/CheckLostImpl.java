package com.robot.bbin.base.basic;

import com.robot.center.util.BoyerMoore;
import com.robot.core.function.base.ICheckLost;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author mrt
 * @Date 2020/6/1 13:06
 * @Version 2.0
 */
@Service
public class CheckLostImpl implements ICheckLost {

    private static final String LOST_FLAG = "請重新登入";
    private static final String LOST_FLAG2 ="System Error";
    private static final String LOST_FLAG3 = "请重新登入";



    @Override
    public boolean isLose(RobotWrapper robotWrapper, StanderHttpResponse standerHttpResponse) {
        Object originalEntity = standerHttpResponse.getOriginalEntity();
        if (null != originalEntity) {
            String entity = (String) originalEntity;
            if (!StringUtils.isEmpty(entity) ) {
                if (-1 != BoyerMoore.find(LOST_FLAG, entity)
                        || -1 != BoyerMoore.find(LOST_FLAG2, entity)
                        || -1 != BoyerMoore.find(LOST_FLAG3, entity)) {
                    return true;
                }
            }
        }
        return false;
    }
}
