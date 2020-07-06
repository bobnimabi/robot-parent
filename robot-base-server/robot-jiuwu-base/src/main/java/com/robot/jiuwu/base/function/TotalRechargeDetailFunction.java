package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.robot.VipTotalAmountDTO;

import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.dto.TotalRechargeDTO;
import com.robot.jiuwu.base.vo.RechargeResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 所有游戏打码总量之和
 */
@Slf4j
@Service
public class TotalRechargeDetailFunction extends AbstractFunction<TotalRechargeDTO,String,RechargeResultVO> {


    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.TOTAL_RECHARGE_DETAIL;
    }

    @Override
    protected IEntity getEntity(TotalRechargeDTO rechargeDTO, RobotWrapper robotWrapper) {

        return UrlEntity.custom(6)
                .add("gameid", rechargeDTO.getUserName())
                .add("start", rechargeDTO.getBeginDate())
                .add("end", rechargeDTO.getEndDate())
                .add("total", "0")
                .add("pageSize", "100")
                .add("currentPage", "1");
    }


    @Override
    protected IResultHandler<String, RechargeResultVO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, RechargeResultVO> {
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }


        @Override
        public Response parse2Obj(StanderHttpResponse<String, RechargeResultVO> shr) {
            String result = shr.getOriginalEntity();
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("未响应");
            }
            RechargeResultVO rechargeResultVO = JSON.parseObject(result, RechargeResultVO.class);
            if (null == rechargeResultVO.getCode()) {
                return Response.FAIL("转换失败");


            }
            return Response.SUCCESS(rechargeResultVO);
        }
    }
}