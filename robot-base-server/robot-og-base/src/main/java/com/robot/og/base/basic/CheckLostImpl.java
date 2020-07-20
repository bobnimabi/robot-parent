package com.robot.og.base.basic;

import com.robot.center.util.BoyerMoore;
import com.robot.core.function.base.ICheckLost;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 全局通用：检查是否掉线
 * @Author mrt
 * @Date 2020/6/1 13:06
 * @Version 2.0
 * 注意：类名不要改变
 */
@Service
public class CheckLostImpl implements ICheckLost {

    private static final String LOST_FLAG = "登录超时,请重新登录!";

    @Override
    public boolean isLose(RobotWrapper robotWrapper, StanderHttpResponse shp) {
        return lost1(shp);
    }

    /**
     * og掉线状态1
     * 返回的HTML里包含：登录超时,请重新登录!
     */
    private boolean lost1(StanderHttpResponse shp) {
        if (shp.getOriginalEntity() instanceof String) {
            String entity = (String)shp.getOriginalEntity();
            if (!StringUtils.isEmpty(entity) && -1 != BoyerMoore.find(LOST_FLAG,entity)) {
                return true;
            }
        }
        return false;
    }
}
