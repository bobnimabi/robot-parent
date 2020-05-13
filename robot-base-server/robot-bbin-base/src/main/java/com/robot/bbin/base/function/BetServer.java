package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.dto.BetDTO;
import com.robot.bbin.base.vo.BetVO;
import com.robot.bbin.base.vo.QueryBalanceVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 下注查询
 */
@Service
public class BetServer extends FunctionBase<BetDTO> {

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<BetDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        BetDTO betDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null,
                createParams(betDTO), null, Parse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.BET_ANALYSIS;
    }

    //组装局查询
    private UrlCustomEntity createParams(BetDTO betDTO) throws Exception{
        return UrlCustomEntity.custom()
                .add("start", betDTO.getStart())
                .add("end", betDTO.getEnd())
                .add("game", betDTO.getGame())
                .add("currency", betDTO.getCurrency())
                .add("gametype", betDTO.getGametype())
                .add("name", betDTO.getName())
                .add("accountType", betDTO.getAccountType())
                .add("analystType", betDTO.getAnalystType());
    }

    /**
     * 响应结果转换类
     */
    private static final class Parse implements IResultParse {
        private static final Parse INSTANCE = new Parse();
        private Parse(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未返回任何响应");
            }
            BetVO betVO = JSON.parseObject(result, BetVO.class);
            if (betVO.getList().size() == 0) {
                return ResponseResult.SUCCESS_MES("无投注记录");
            }
            return ResponseResult.SUCCESS(betVO);
        }
    }
}
