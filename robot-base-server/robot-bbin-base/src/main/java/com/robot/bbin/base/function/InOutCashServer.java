package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.gpk.base.ao.InOutCashAO;
import com.robot.bbin.base.bo.InOutCashData;
import com.robot.bbin.base.bo.InOutCashBO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
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
public class InOutCashServer extends AbstractFunction<InOutCashAO,String, InOutCashBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.IN_OUT_CASH;
    }

    @Override
    protected ICustomEntity getEntity(InOutCashAO ioc, RobotWrapper robotWrapper) {
        return UrlEntity.custom(14)
                .add("start", ioc.getStart())
                .add("end", ioc.getEnd())
                .add("methed", ioc.getMethed())
                .add("amount_value", ioc.getAmount_value())
                .add("amount_than", ioc.getAmount_than())
                .add("times", ioc.getTimes())
                .add("than", ioc.getThan())
                .add("Currency", ioc.getCurrency())
                .add("sortCol", ioc.getSortCol())
                .add("sort", ioc.getSort())
                .add("name", ioc.getName())
                .add("accountType", ioc.getAccountType())
                .add("analystType", ioc.getAnalystType())
                .add("page", ioc.getPage());
    }

    @Override
    protected IResultHandler<String, InOutCashBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果处理
     */
    private static final class ResultHandler implements IResultHandler<String, InOutCashBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, InOutCashBO> shr) {
            String result = shr.getOriginalEntity();
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("出入款统计:未响应结果");
            }
            if (result.contains("error")) {
                return Response.FAIL(result);
            }
            InOutCashBO inOutCashVO = new InOutCashBO();
            JSONObject jsonObject = JSON.parseObject(result);
            Integer  total_page = jsonObject.getInteger("total_page");
            inOutCashVO.setPage(total_page);
            if (total_page == 0) {
                return Response.SUCCESS(inOutCashVO);
            }
            List<InOutCashData> list = new ArrayList<>(1);
            Map<String,Map<String,String>> data = (Map<String,Map<String,String>>)jsonObject.get("data");
            data.forEach((k,v)->{
                String vvString = JSON.toJSONString(v);
                InOutCashData vo = JSON.parseObject(vvString, InOutCashData.class);
                list.add(vo);
            });
            inOutCashVO.setList(list);
            return Response.SUCCESS(inOutCashVO);
        }
    }
}
