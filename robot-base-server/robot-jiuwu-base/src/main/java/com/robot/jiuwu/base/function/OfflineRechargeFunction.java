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
import com.robot.jiuwu.base.vo.OfflineRechargeVO;
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
public class OfflineRechargeFunction extends AbstractFunction<OfflineDataDTO,String,OfflineRechargeVO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.OFFLINE_RECHARGE;
    }

    @Override
    protected IEntity getEntity(OfflineDataDTO offlineDataDTO, RobotWrapper robotWrapper) {

        int[] types = offlineDataDTO.getTypes();
        BigDecimal minamount = offlineDataDTO.getMinamount();
        BigDecimal maxamount = offlineDataDTO.getMaxamount();

        return JsonEntity.custom(1)
                .add("current", offlineDataDTO.getCurrent())
                .add("size", offlineDataDTO.getSize())
                .add("recordid", offlineDataDTO.getRecordid())
                .add("username", offlineDataDTO.getUsername())
                .add("types", null != types ? types : "")
                .add("gameid", offlineDataDTO.getGameid())
                .add("memberOrder", offlineDataDTO.getMemberOrder())
                .add("orderdatebegin", offlineDataDTO.getOrderdatebegin())
                .add("orderdateend", offlineDataDTO.getOrderdateend())
                .add("minamount", null != minamount ? MoneyUtil.formatYuan(minamount).toString() : "")
                .add("maxamount", null != maxamount ? MoneyUtil.formatYuan(offlineDataDTO.getMaxamount()).toString() : "")
                .add("remark", offlineDataDTO.getRemark());
    }

    @Override
    protected IResultHandler<String, OfflineRechargeVO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：
     * 存在返回：
     *      {"code":0,"IsSuccess":true,}
     * 不存在返回：
     *      {"code":1,"IsSuccess":false,}
     */
    private static final class ResultHandler implements IResultHandler<String,OfflineRechargeVO>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, OfflineRechargeVO> shr) {
            String result = shr.getOriginalEntity();
            log.info("查询会员存在功能响应：{}", result);
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("未响应");
            }

            OfflineRechargeVO OfflineRechargeVO = JSON.parseObject(result, OfflineRechargeVO.class);
            if (null == OfflineRechargeVO.getCode()) {
                return Response.FAIL("转换失败");
            }
            return Response.SUCCESS(OfflineRechargeVO);

        }
    }

}
