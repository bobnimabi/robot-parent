package com.robot.suxiao.base.controller;

import bom.bbin.common.dto.income.robot.CancelCardDTO;
import com.alibaba.fastjson.JSON;
import com.bbin.common.constant.RabbitMqConstants;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.response.ResponseResult;
import com.rabbitmq.client.Channel;
import com.robot.center.constant.RobotConsts;
import com.robot.center.controller.RobotControllerBase;
import com.robot.center.dispatch.ITaskPool;
import com.robot.center.execute.TaskWrapper;
import com.robot.center.function.ParamWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.center.util.MoneyUtil;
import com.robot.suxiao.base.basic.FunctionEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Duration;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
public class SuXiaoControllerBase extends RobotControllerBase {

    /**
     * 销卡（单张）
     * @return
     * @throws Exception
     */
    @PostMapping("/cancelCard")
    public ResponseResult cancelCard(@Valid @RequestBody CancelCardDTO cardDTO) throws Exception{
        return distribute(new ParamWrapper<CancelCardDTO>(cardDTO), FunctionEnum.CANCEL_CARDS_ERVER);
    }
}
