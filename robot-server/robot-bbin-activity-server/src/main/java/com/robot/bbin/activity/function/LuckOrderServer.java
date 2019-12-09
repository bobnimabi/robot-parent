package com.robot.bbin.activity.function;

import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.bbin.activity.dto.OrderNoQueryDTO;
import com.robot.bbin.activity.vo.JuQueryVO;
import com.robot.bbin.activity.vo.LuckOrderQueryVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.bbin.activity.basic.ActionEnum;
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

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
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

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.JU_QUERY;
    }

}
