package com.robot.jiuwu.activity.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.robot.VipTotalAmountDTO;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.vo.VipTotalAmountVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.function.QueryUserDetailServer;
import com.robot.jiuwu.base.vo.QueryUserResultVO;
import com.robot.jiuwu.base.vo.UserInfoDetailResultVO;
import com.robot.jiuwu.base.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户的VIP
 */
@Slf4j
@Service
public class QueryVipAmountServer extends FunctionBase<VipTotalAmountDTO> {
    @Autowired
    private QueryUserDetailServer queryUserDetailServer;
    @Autowired
    private TotalRechargeServer totalRechargeServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<VipTotalAmountDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        VipTotalAmountDTO vipTotalAmountDTO = paramWrapper.getObj();
        // 查询打码总额
        ResponseResult totalRechargeResult = getTotalRecharge(paramWrapper, robotWrapper);
        if (!totalRechargeResult.isSuccess()) {
            return totalRechargeResult;
        }

        VipTotalAmountVO vipTotalAmountVO = (VipTotalAmountVO) totalRechargeResult.getObj();

        // 查询会员VIP等级
        ResponseResult vipResut = getVip(vipTotalAmountDTO.getUserName(),robotWrapper);
        if (!vipResut.isSuccess()) {
            return vipResut;
        }

        Integer VIP = (Integer) vipResut.getObj();
        vipTotalAmountVO.setVip(VIP+"");
        return ResponseResult.SUCCESS(vipTotalAmountVO);
    }

    // 查询打码总额
    private ResponseResult getTotalRecharge(ParamWrapper<VipTotalAmountDTO> paramWrapper,RobotWrapper robotWrapper) throws Exception {
        return totalRechargeServer.doFunction(paramWrapper, robotWrapper);
    }

    // 查询会员VIP等级
    private ResponseResult getVip(String username,RobotWrapper robotWrapper) throws Exception {
        ResponseResult responseResult = queryUserDetailServer.doFunction(new ParamWrapper<String>(username), robotWrapper);
        if (!responseResult.isSuccess()) {
            return responseResult;
        }

        UserInfoDetailResultVO resultVO = (UserInfoDetailResultVO) responseResult.getObj();
        if (!Constant.SUCCESS.equals(resultVO.getCode())) {
            return ResponseResult.FAIL(resultVO.getMsg());
        }
        Integer tempCodingNum = resultVO.getData().getAccountsInfoExt().getTempCodingNum();
        return ResponseResult.SUCCESS(tempCodingNum);
    }

    @Override
    public IActionEnum getActionEnum() {
        return null;
    }


    // 响应结果转换
    private static final class QueryUserParser implements IResultParse{
        private static final QueryUserParser INSTANCE = new QueryUserParser();
        private QueryUserParser(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            QueryUserResultVO usesrResultVO = JSON.parseObject(result, QueryUserResultVO.class);
            if (null == usesrResultVO.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(usesrResultVO);
        }
    }
}
