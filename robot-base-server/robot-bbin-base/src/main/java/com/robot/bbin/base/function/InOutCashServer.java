package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.dto.InOutCashDTO;
import com.robot.bbin.base.vo.InOutCashData;
import com.robot.bbin.base.vo.InOutCashVO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 出入款统计
 * 注意：出款和入款的统计响应结果是不一样的，此处处理的是入款
 */
@Service
public class InOutCashServer extends FunctionBase<InOutCashDTO> {

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<InOutCashDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        InOutCashDTO cashDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null,
                createParams(cashDTO), null, Parse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.IN_OUT_CASH;
    }

    //组装局查询
    private UrlCustomEntity createParams(InOutCashDTO inOutCashDTO) throws Exception{
        return UrlCustomEntity.custom()
                .add("start", inOutCashDTO.getStart())
                .add("end", inOutCashDTO.getEnd())
                .add("methed", inOutCashDTO.getMethed())
                .add("amount_value", inOutCashDTO.getAmount_value())
                .add("amount_than", inOutCashDTO.getAmount_than())
                .add("times", inOutCashDTO.getTimes())
                .add("than", inOutCashDTO.getThan())
                .add("Currency", inOutCashDTO.getCurrency())
                .add("sortCol", inOutCashDTO.getSortCol())
                .add("sort", inOutCashDTO.getSort())
                .add("name", inOutCashDTO.getName())
                .add("accountType", inOutCashDTO.getAccountType())
                .add("analystType", inOutCashDTO.getAnalystType())
                .add("page", inOutCashDTO.getPage());
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
                return ResponseResult.FAIL("未返回任何结果");
            }
            if (result.contains("error")) {
                return ResponseResult.FAIL(result);
            }
            InOutCashVO inOutCashVO = new InOutCashVO();
            JSONObject jsonObject = JSON.parseObject(result);
            Integer  total_page = jsonObject.getInteger("total_page");
            if (0 == total_page) {
                inOutCashVO.setPage(0);
                return ResponseResult.FAIL("无记录");
            }
            List<InOutCashData> list = new ArrayList<>(1);
            Map<String,Map<String,String>> data = (Map<String,Map<String,String>>)jsonObject.get("data");
            data.forEach((k,v)->{
                String vvString = JSON.toJSONString(v);
                InOutCashData vo = JSON.parseObject(vvString, InOutCashData.class);
                list.add(vo);
            });
            inOutCashVO.setList(list);

            return ResponseResult.SUCCESS(inOutCashVO);
        }
    }
}
