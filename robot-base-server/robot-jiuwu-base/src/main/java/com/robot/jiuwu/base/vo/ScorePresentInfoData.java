package com.robot.jiuwu.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by mrt on 2019/12/31 0031 13:26
 */
@Data
public class ScorePresentInfoData {
    @JSONField(name="CollectDate")
    private String CollectDate;
    @JSONField(name="UserID")
    private String UserID;
    @JSONField(name="ID")
    private String ID;
    @JSONField(name="UserAccount")
    private String UserAccount;
    @JSONField(name="PresentScore")
    private String PresentScore;
    @JSONField(name="Remark")
    private String Remark;
}
