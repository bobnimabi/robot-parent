package com.robot.xiaoka.income.controller;

import bom.bbin.common.dto.income.robot.CancelCardDTO;
import bom.bbin.common.dto.income.robot.WithDrawDTO;
import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.center.dispatch.ITaskPool;
import com.robot.center.function.ParamWrapper;
import com.robot.center.util.MoneyUtil;
import com.robot.suxiao.base.basic.FunctionEnum;
import com.robot.suxiao.base.controller.SuXiaoControllerBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
@RestController
public class Controller extends SuXiaoControllerBase {

    /**
     * 提现
     * @return
     * @throws Exception
     */
    @PostMapping("/withdraw")
    public ResponseResult withdraw(@Valid @RequestBody WithDrawDTO withDrawDTO) throws Exception{
        return distribute(new ParamWrapper<WithDrawDTO>(withDrawDTO), FunctionEnum.WITHDRAW_FINAL_SERVER);
    }
}
