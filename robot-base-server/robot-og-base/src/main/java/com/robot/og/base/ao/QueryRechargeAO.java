package com.robot.og.base.ao;

import lombok.Data;

/**
 * <p>
 *  查询充值参数对象
 * </p>
 *
 * @author tank
 * @date 2020/7/16
 */
@Data
public class QueryRechargeAO {
        private String type;
        private String tradeTypes;
        private String isPostback;
        private String orderField;
        private String sortBy;
        private String levelManagementIds;
        private String memberAccounts;
        private String isBatchAccount;
        private String selDate;
        private String memType;
        private String startDate;
        private String endDate;
        private String actType;
        //等于会员账号
        private String memberNo;
        private String bettingCode;
        private String pageSize;



}
