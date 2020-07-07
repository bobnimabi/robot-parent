package com.robot.jiuwu.activity.server;

import com.bbin.common.dto.robot.VipTotalAmountDTO;

import com.bbin.common.vo.VipTotalAmountVO;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;

import com.robot.jiuwu.base.common.Constant;
import com.robot.jiuwu.base.function.QueryUserDetailFunction;
import com.robot.jiuwu.base.vo.UserInfoDetailResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户的VIP
 */
@Slf4j
@Service
public class QueryVipAmountServer implements IAssemFunction<VipTotalAmountDTO> {

    @Autowired
    private TotalRechargeServer totalRechargeServer;

    @Autowired
    private QueryUserDetailFunction queryUserDetailFunction;

    @Override
    public Response doFunction(ParamWrapper<VipTotalAmountDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

            VipTotalAmountDTO vipTotalAmountDTO = paramWrapper.getObj();
            // 查询打码总额
        Response totalBetResult = getTotalRecharge(paramWrapper, robotWrapper);
            if (!totalBetResult.isSuccess()) {
                return totalBetResult;
            }

            VipTotalAmountVO vipTotalAmountVO = (VipTotalAmountVO) totalBetResult.getObj();

            // 查询会员VIP等级
        Response vipResut = getVip(vipTotalAmountDTO.getUserName(),robotWrapper);
            if (!vipResut.isSuccess()) {
                return vipResut;
            }

            Integer VIP = (Integer) vipResut.getObj();
            vipTotalAmountVO.setVip(VIP+"");
            return Response.SUCCESS(vipTotalAmountVO);
        }

        // 查询打码总额
        private Response getTotalRecharge(ParamWrapper<VipTotalAmountDTO> paramWrapper,RobotWrapper robotWrapper) throws Exception {
            return totalRechargeServer.doFunction(paramWrapper, robotWrapper);
        }

        // 查询会员VIP等级
        private Response getVip(String username,RobotWrapper robotWrapper) throws Exception {
            Response responseResult = queryUserDetailFunction.doFunction(new ParamWrapper<String>(username), robotWrapper);
            if (!responseResult.isSuccess()) {
                return responseResult;
            }

            UserInfoDetailResultVO resultVO = (UserInfoDetailResultVO) responseResult.getObj();
            if (!Constant.SUCCESS.equals(resultVO.getCode())) {
                return Response.FAIL(resultVO.getMsg());
            }
            Integer tempCodingNum = resultVO.getData().getInfo().getMemberOrder();
            return Response.SUCCESS(tempCodingNum);
    }

}
