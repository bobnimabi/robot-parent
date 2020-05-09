package com.robot.gpk.activity.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.center.dispatch.ITaskPool;
import com.robot.center.function.ParamWrapper;
import com.robot.center.util.MoneyUtil;
import com.robot.gpk.base.basic.FunctionEnum;
import com.robot.gpk.base.controller.GpkController;
import com.robot.gpk.base.dto.PayMoneyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
@RestController
public class GpkActivityController extends GpkController {

    // 测试打款
    @PostMapping("/tempPay")
    public ResponseResult tempPay(@RequestBody PayMoneyDTO payMoneyDTO) throws Exception {
        if (null == payMoneyDTO
                || StringUtils.isEmpty(payMoneyDTO.getUsername())
                || null == payMoneyDTO.getPaidAmount()
                || StringUtils.isEmpty(payMoneyDTO.getMemo())
                || StringUtils.isEmpty(payMoneyDTO.getOutPayNo())
        ) ResponseResult.FAIL("参数不全");
        log.info("mq打款入参：{}", JSON.toJSONString(payMoneyDTO));

        if (payMoneyDTO.getPaidAmount().compareTo(BigDecimal.ZERO) <= 0) {
            ResponseResult.FAIL("金额不能小于等于0");
        }

        payMoneyDTO.setPaidAmount(MoneyUtil.formatYuan(payMoneyDTO.getPaidAmount()));
        payMoneyDTO.setUsername(payMoneyDTO.getUsername().trim());
        String externalNo = payMoneyDTO.getOutPayNo();
        if (StringUtils.isNotBlank(externalNo)) {
            boolean isRedo = isRedo(externalNo);
            if (isRedo) {
                log.info("该外部订单号已经存在,将不执行,externalNo:{},功能参数:{}", externalNo, JSON.toJSONString(payMoneyDTO));
                return ResponseResult.FAIL("重复打款");
            }
        }
        return distribute(new ParamWrapper<PayMoneyDTO>(payMoneyDTO), FunctionEnum.PAY_SERVER);
    }
}
