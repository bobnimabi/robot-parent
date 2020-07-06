package com.robot.jiuwu.base.function;

import com.alibaba.fastjson.JSON;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.jiuwu.base.basic.PathEnum;
import com.robot.jiuwu.base.dto.OfflineDataDTO;
import com.robot.jiuwu.base.dto.OnlineRechargeDTO;
import com.robot.jiuwu.base.vo.OfflineRechargeVO;
import com.robot.jiuwu.base.vo.OnlineRechargeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 线下充值总金额
 */
@Slf4j
@Service
public class OnllineRechargeFunction extends AbstractFunction<OnlineRechargeDTO,String, OnlineRechargeVO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.ONLINE_RECHARGE;
    }

    @Override
    protected IEntity getEntity(OnlineRechargeDTO onlineRechargeDTO, RobotWrapper robotWrapper) {

        BigDecimal amount_start = onlineRechargeDTO.getAmount_start();
        BigDecimal amount_end = onlineRechargeDTO.getAmount_end();

        return JsonEntity.custom(18)
                .add("current", onlineRechargeDTO.getCurrent() + "")
                .add("size", onlineRechargeDTO.getSize() + "")
                .add("orderid", onlineRechargeDTO.getOrderid())
                .add("gameid", onlineRechargeDTO.getGameid())
                .add("amount_start", null != amount_start ? MoneyUtil.formatYuan(amount_start).toString() : "")
                .add("amount_end", null != amount_end ? MoneyUtil.formatYuan(amount_end).toString() : "")
                .add("orderstatus", onlineRechargeDTO.getOrderstatus())
                .add("paydate_start", onlineRechargeDTO.getPaydate_start())
                .add("paydate_end", onlineRechargeDTO.getPaydate_end())
                .add("orderdate_start", onlineRechargeDTO.getOrderdate_start())
                .add("orderdate_end", onlineRechargeDTO.getOrderdate_end())
                .add("sh", onlineRechargeDTO.getSh())
                .add("source", onlineRechargeDTO.getSource())
                .add("ordertype", onlineRechargeDTO.getOrdertype())
                .add("shareid", onlineRechargeDTO.getShareid())
                .add("onlineid", onlineRechargeDTO.getOnlineid())
                .add("payaddress", onlineRechargeDTO.getPayaddress())
                .add("memberOrder", onlineRechargeDTO.getMemberOrder());
    }

    @Override
    protected IResultHandler<String, OnlineRechargeVO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：
     * 存在返回：
     *      {"code":0,"IsSuccess":true,}
     * 不存在返回：
     *      {"code":1,"IsSuccess":false,}
     */
    private static final class ResultHandler implements IResultHandler<String,OnlineRechargeVO>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, OnlineRechargeVO> shr) {
            String result = shr.getOriginalEntity();
            log.info("查询会员存在功能响应：{}", result);
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("查询会员未响应");
            }

            OnlineRechargeVO rechargeVO = JSON.parseObject(result, OnlineRechargeVO.class);
            if (null == rechargeVO.getCode()) {
                return Response.FAIL("转换失败");
            }
            return Response.SUCCESS(rechargeVO);



                
        }
    }

}
