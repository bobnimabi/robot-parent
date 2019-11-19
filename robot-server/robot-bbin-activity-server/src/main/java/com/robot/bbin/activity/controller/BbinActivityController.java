package com.robot.bbin.activity.controller;

import com.bbin.common.response.ResponseResult;
import com.robot.bbin.activity.basic.FunctionEnum;
import com.robot.bbin.activity.dto.OrderNoQueryDTO;
import com.robot.center.controller.RobotControllerBase;
import com.robot.center.function.ParamWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
@RestController
public class BbinActivityController extends RobotControllerBase {

    /**
     * 幸运注单
     * 1.会员在【PT电子、SG电子、RT电子、JDB电子、CQ9】中进行投注
     * 2.此活动仅限老虎机与经典老虎机游戏中产生的注单
     */
    @PostMapping("queryOrderNo")
    public ResponseResult queryOrderNo(@RequestBody OrderNoQueryDTO orderNoQueryDTO) throws Exception{
        if (null == orderNoQueryDTO
                || StringUtils.isEmpty(orderNoQueryDTO.getOrderNo())
                || null == orderNoQueryDTO.getStartDate()
                || null == orderNoQueryDTO.getEndDate()
                || StringUtils.isEmpty(orderNoQueryDTO.getGameCode())//平台编码
        ) return ResponseResult.FAIL("参数不全");
        return distribute(new ParamWrapper<OrderNoQueryDTO>(orderNoQueryDTO), FunctionEnum.LUCK_ORDER_SERVER);
    }

    /**
     * 消消乐查询
     * @param queryDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/getEliminateToLe")
    public ResponseResult getEliminateToLe(@RequestBody OrderNoQueryDTO queryDTO) throws Exception {
        return distribute(new ParamWrapper<OrderNoQueryDTO>(queryDTO), FunctionEnum.BREAK_SERVER);
    }




}
