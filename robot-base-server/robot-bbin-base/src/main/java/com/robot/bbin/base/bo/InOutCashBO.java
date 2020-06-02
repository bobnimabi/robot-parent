package com.robot.bbin.base.bo;

import lombok.Data;

import java.util.List;

/**
 * Created by mrt on 2020/5/13 14:09
 */
@Data
public class InOutCashBO {
    private int page;
    private List<InOutCashData> list;

}
