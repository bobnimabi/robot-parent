package com.robot.jiuwu.activity.vo;

import com.robot.jiuwu.login.vo.ParentResultVO;
import lombok.Data;

/**
 * Created by mrt on 2019/12/28 0028 16:22
 * 查询用户是否存在
 */
@Data
public class QueryUserResultVO extends ParentResultVO {
    private QueryUserData data;
}
