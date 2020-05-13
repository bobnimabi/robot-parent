package com.robot.bbin.base.function;

import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.dto.OrderNoQueryDTO;
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

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 局查询弹窗（）
 */
@Service
public class JuQueryDetailServer extends FunctionBase<OrderNoQueryDTO> {

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.GET, action, null,
                createBodyParams(queryDTO), null, ResultParse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.JU_QUERY_DETAIL;
    }

    //组装局查询
    private UrlCustomEntity createBodyParams(OrderNoQueryDTO queryDTO) throws Exception{
        return UrlCustomEntity.custom()
                .add("lang", "cn")
                .add("wid", queryDTO.getOrderNo())// 注单编号
                .add("id", queryDTO.getPageId()) // 平台编码
                .add("gametype", queryDTO.getGameType()) // 游戏编码
                .add("key", queryDTO.getKey()); // 页面携带参数
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultParse implements IResultParse {
        private static final ResultParse INSTANCE = new ResultParse();
        private ResultParse(){}

        @Override
        public ResponseResult parse(String result) {
            return ResponseResult.SUCCESS(result);
        }
    }
}
