package com.robot.jiuwu.activity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by mrt on 2019/12/31 0031 12:55
 * 查询用户细节的银行信息
 */
@Data
public class BankData {
    @JSONField(name="UserID")
    private Long UserID;
    private Long id;
    @JSONField(name="OpenBank")
    private String OpenBank;
    @JSONField(name="SubBank")
    private String SubBank;
    @JSONField(name="Card")
    private String Card;
    @JSONField(name="Name")
    private String Name;
}

