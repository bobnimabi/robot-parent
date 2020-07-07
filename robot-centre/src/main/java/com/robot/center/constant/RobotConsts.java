package com.robot.center.constant;

import com.robot.core.common.RedisConsts;

/**
 * Created by mrt on 2019/7/4 0004 下午 6:02
 */
public class RobotConsts {




	/**
     * 请求响应状态：1发送中 2响应成功 0 响应失败
     * tenant_robot_record
     */
    public static class RobotRecord{
        //1发送中
        public static final int SENDING = 1;
        //2响应成功
        public static final int RESP_SUCCESS = 2;
        //0响应失败
        public static final int RESP_FAILER = -1;
    }

    /**
     * function_code的配置
     * 功能：1.活动  2.出款  3.入款
     */
    public static class FUNCTION_CODE{
        public static final String ACTIVITY = "1";
        public static final String WITHDRAW = "2";
        public static final String INCOME = "3";
    }

    /**
     * platform_id的配置
     */
    public static class PLATFORM_ID{
        public static final String OG = "1";
        public static final String BBIN = "2";
        public static final String FU_CAI = "3";
        public static final String HONG_BO = "4";
        public static final String GPK = "5";
        public static final String YUN_SHAN_FU = "6";
        public static final String JIU_WU_CARD = "7";
        public static final String REN_REN_XIAO_KA = "8";
    }

    // 本项目前缀8
    public static final String ROBOT_PROJECT_PERFIX = "S_ROBOT:";

    // 图片验证码缓存14
    public static final String CAPTCHA_TOKEN = ROBOT_PROJECT_PERFIX + "CAPTCHA_TOKEN:";


    // 机器人ID
    public static final String ROBOT_ID = "ROBOT_ID";
}
