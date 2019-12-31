package com.robot.jiuwu.activity.vo;

import lombok.Data;

/**
 * Created by mrt on 2019/12/31 0031 14:53
 */
@Data
public class UserInfoDetailData {
    private Boolean updateBank;
    private Boolean updateAlipay;
    private Boolean updatePhone;
    private BankData bank;
    private AccountsInfoExtData accountsInfoExt;
    private ScorePresentInfoData scorePresentInfo;
    private InfoData info;
}
