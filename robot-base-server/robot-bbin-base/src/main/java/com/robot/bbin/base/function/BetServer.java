package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.dto.BetDTO;
import com.robot.bbin.base.vo.BetVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.dto.Response;
import com.robot.code.entity.TenantRobotAction;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 下注查询
 */
@Service
public class BetServer extends AbstractFunction<BetDTO,String,List<BetVO>> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.BET_ANALYSIS;
    }

    @Override
    protected ICustomEntity getEntity(BetDTO betDTO) {
        return UrlEntity.custom(8)
                .add("start", betDTO.getStart())
                .add("end", betDTO.getEnd())
                .add("game", betDTO.getGame())
                .add("currency", betDTO.getCurrency())
                .add("gametype", betDTO.getGametype())
                .add("name", betDTO.getName())
                .add("accountType", betDTO.getAccountType())
                .add("analystType", betDTO.getAnalystType());
    }

    @Override
    protected IResultHandler<String, List<BetVO>> getResultHandler() {
        return null;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,List<BetVO>> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未返回任何响应");
            }
            if (result.contains("error")) {
                return ResponseResult.FAIL(result);
            }
            List<BetVO> betVOS = JSON.parseArray(result, BetVO.class);
            if (betVOS.size() == 0) {
                return ResponseResult.SUCCESS_MES("无投注记录");
            }
            return ResponseResult.SUCCESS(betVOS);
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, List<BetVO>> shr) {

            return null;
        }
    }
}
