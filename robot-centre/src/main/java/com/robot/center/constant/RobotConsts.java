package com.robot.center.constant;

/**
 * Created by mrt on 2019/7/4 0004 下午 6:02
 */
public class RobotConsts {

    // 本项目前缀
    public static final String ROBOT_PROJECT_PERFIX = "S_ROBOT:";



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

        //状态集合
        public static final Integer[] statusArr = {SENDING, RESP_SUCCESS, RESP_FAILER};
    }

    /**
     * function_code的配置
     * 功能：1.活动  2.出款  3.入款
     */
    public static class FUNCTION_CODE{
        public static final long ACTIVITY = 1;
        public static final long WITHDRAW = 2;
        public static final long INCOME = 3;

        //状态集合
        public static final long[] statusArr = {ACTIVITY, WITHDRAW, INCOME};
    }

    /**
     * platform_id的配置
     * 功能：1.活动  2.出款  3.入款
     */
    public static class PLATFORM_ID{
        public static final long OG = 1;
        public static final long BBIN = 2;
        public static final long FU_CAI = 3;
        public static final long HONG_BO = 4;
        public static final long GPK = 5;
        public static final long YUN_SHAN_FU = 6;

        //状态集合
        public static final long[] statusArr = {OG, BBIN, FU_CAI, HONG_BO, GPK, YUN_SHAN_FU};
    }



}
