package com.robot.core.robot.manager;

import org.springframework.util.Assert;

/**
 * @Author mrt
 * @Date 2020/5/25 19:59
 * @Version 2.0
 */
public interface ICloudIdCard {

    /**
     * 获取idCard
     * @return
     */
    String getIdCard(long robotId);

    /**
     * 设置IdCard
     * @param robotId
     * @param newIdCard
     */
    void setIdCard(long robotId,String newIdCard);

    /**
     * 删除idCard
     * @param robotId
     */
    void delIdCard(long robotId);
}
