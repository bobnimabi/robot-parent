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
     * 返回null：
     *  1.过期
     *  2.被删除
     */
    String getIdCard(long robotId);

    /**
     * 设置IdCard
     * @param robotId
     * @param newIdCard
     * 情景：
     *  1.登录的时候
     */
    boolean setIdCard(long robotId,String newIdCard);

    /**
     * 删除idCard
     * 等效于：删除了Cookie和Token
     * @param robotId
     */
    boolean delIdCard(long robotId);
}
