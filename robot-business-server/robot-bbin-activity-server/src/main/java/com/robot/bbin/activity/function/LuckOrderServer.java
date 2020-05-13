package com.robot.bbin.activity.function;

import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.dto.OrderNoQueryDTO;
import com.robot.bbin.base.function.JuQueryRoundServer;
import com.robot.bbin.base.vo.JuQueryVO;
import com.robot.bbin.base.vo.LuckOrderQueryVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/15/2019 6:39 PM
 * 幸运注单
 */
@Service
public class LuckOrderServer extends FunctionBase<OrderNoQueryDTO> {
    @Autowired
    private JuQueryServer juQueryServer;
    @Autowired
    private JuQueryRoundServer juQueryRoundServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();
        switch (queryDTO.getGameCode()) {
            case "42": // 申博电子
                return shenBoNo(paramWrapper,robotWrapper,action);
            case "52": // CQ9电子
                return cq9QueryNo(paramWrapper,robotWrapper,action);
            default: // 其他电子：BB电子、PT电子、JDB电子、SG电子
                return otherNo(paramWrapper, robotWrapper, action);
        }
    }

    /**
     * 注单查询
     * 其他电子：BB电子、PT电子、JDB电子、SG电子
     */
    private ResponseResult otherNo(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();
        ResponseResult responseResult = juQueryServer.doFunctionFinal(paramWrapper, robotWrapper, action);
        if (!responseResult.isSuccess()) {
            return responseResult;
        }
        JuQueryVO juQueryVO = (JuQueryVO) responseResult.getObj();
        if (!queryDTO.getUserName().equals(juQueryVO.getUserName())) {
            return ResponseResult.FAIL("会员账号不匹配，传入：" + queryDTO.getUserName() + " 实际：" + juQueryVO.getUserName());
        }
        LuckOrderQueryVO luckOrderQueryVO = MyBeanUtil.copyProperties(juQueryVO, LuckOrderQueryVO.class);
        return ResponseResult.SUCCESS(luckOrderQueryVO);
    }

    /**
     * 申博电子场次查询（场次为申博平台的注单号）
     */
    private ResponseResult shenBoNo(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();
        ResponseResult responseResult = juQueryRoundServer.doFunctionFinal(paramWrapper, robotWrapper, action);
        if (!responseResult.isSuccess()) {
            return responseResult;
        }
        JuQueryVO juQueryVO = (JuQueryVO) responseResult.getObj();
        if (!queryDTO.getUserName().equals(juQueryVO.getUserName())) {
            return ResponseResult.FAIL("会员账号不匹配，传入：" + queryDTO.getUserName() + " 实际：" + juQueryVO.getUserName());
        }
        LuckOrderQueryVO luckOrderQueryVO = MyBeanUtil.copyProperties(juQueryVO, LuckOrderQueryVO.class);
        luckOrderQueryVO.setPlatFormOrderNo(queryDTO.getOrderNo());
        return ResponseResult.SUCCESS(luckOrderQueryVO);
    }

    /**
     * CQ9电子注单查询
     */
    private ResponseResult cq9QueryNo(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) {
        return null;
    }


    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.JU_QUERY;
    }

}
