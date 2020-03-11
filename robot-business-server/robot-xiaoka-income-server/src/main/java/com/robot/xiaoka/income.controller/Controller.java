package com.robot.xiaoka.income.controller;

import bom.bbin.common.dto.income.robot.WithDrawDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.center.function.ParamWrapper;
import com.robot.suxiao.base.basic.FunctionEnum;
import com.robot.suxiao.base.controller.SuXiaoControllerBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
