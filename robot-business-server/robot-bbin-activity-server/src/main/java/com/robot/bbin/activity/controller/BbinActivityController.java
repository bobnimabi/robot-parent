package com.robot.bbin.activity.controller;

import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.FunctionEnum;
import com.robot.bbin.base.controller.BbinController;
import com.robot.bbin.base.dto.GameChild;
import com.robot.bbin.base.dto.OrderNoQueryDTO;
import com.robot.center.controller.RobotControllerBase;
import com.robot.center.dispatch.ITaskPool;
import com.robot.center.function.ParamWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 11/14/2019 3:59 PM
 */
@Slf4j
@RestController
public class BbinActivityController extends BbinController {

    /**
     * 幸运注单
     * 1.会员在【PT电子、SG电子、RT电子、JDB电子、CQ9】中进行投注
     * 2.此活动仅限老虎机与经典老虎机游戏中产生的注单
     */
    @PostMapping("/queryOrderNo")
    public ResponseResult queryOrderNo(@RequestBody OrderNoQueryDTO orderNoQueryDTO) throws Exception{
        if (null == orderNoQueryDTO
                || StringUtils.isEmpty(orderNoQueryDTO.getOrderNo())
                || StringUtils.isEmpty(orderNoQueryDTO.getGameCode()) // 平台编码
        ) return ResponseResult.FAIL("参数不全");
        return distribute(new ParamWrapper<OrderNoQueryDTO>(orderNoQueryDTO), FunctionEnum.LUCK_ORDER_SERVER);
    }

    /**
     * 消消乐查询OG专用
     * @param queryDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/getEliminateToLe")
    public ResponseResult getEliminateToLe(@RequestBody OrderNoQueryDTO queryDTO) throws Exception {
        if (null == queryDTO
                || StringUtils.isEmpty(queryDTO.getOrderNo())
                || null == queryDTO.getStartDate()
                || null == queryDTO.getEndDate()
                || StringUtils.isEmpty(queryDTO.getGameCode())//平台编码
        ) return ResponseResult.FAIL("参数不全");
        queryDTO.setIs_BBIN(false);
        return distribute(new ParamWrapper<OrderNoQueryDTO>(queryDTO), FunctionEnum.BREAK_SERVER);
    }

    /**
     * 消消乐查询BBIN专用
     * @param queryDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/getEliminateToLe2")
    public ResponseResult getEliminateToLe2(@RequestBody OrderNoQueryDTO queryDTO) throws Exception {
        if (null == queryDTO
                || StringUtils.isEmpty(queryDTO.getOrderNo())
                || null == queryDTO.getStartDate()
                || null == queryDTO.getEndDate()
                || StringUtils.isEmpty(queryDTO.getGameCode())//平台编码
        ) return ResponseResult.FAIL("参数不全");
        queryDTO.setIs_BBIN(true);
        return distribute(new ParamWrapper<OrderNoQueryDTO>(queryDTO), FunctionEnum.BREAK_SERVER);
    }

    @ApiOperation("机器人：获取投注、亏损、充值信息")
    @PostMapping("/getTotalAmount")
    public ResponseResult getTotalAmount(@RequestBody BreakThroughDTO dto) throws Exception {
        if (StringUtils.isEmpty(dto.getUserName())) {
            return ResponseResult.FAIL("userName为空");
        }
        if (StringUtils.isEmpty(dto.getBeginDate())) {
            return ResponseResult.FAIL("起始时间为空");
        }
        if (StringUtils.isEmpty(dto.getEndDate())) {
            return ResponseResult.FAIL("结束时间为空");
        }

        return distribute(new ParamWrapper<BreakThroughDTO>(dto), FunctionEnum.BET_AMOUNT_AND_RECHARGE_SERVER);
    }
}
