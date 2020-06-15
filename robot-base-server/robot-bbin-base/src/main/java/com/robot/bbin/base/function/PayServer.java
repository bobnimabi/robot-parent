package com.robot.bbin.base.function;

import com.robot.bbin.base.basic.PathEnum;
import com.robot.gpk.base.ao.PayAO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.HtmlResponseHandler;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 充值
 */
@Slf4j
@Service
public class PayServer extends AbstractFunction<PayAO,String,Object> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.PAY;
    }

    @Override
    protected ICustomEntity getEntity(PayAO payDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(15)
                .add("search_name", payDTO.getSearch_name())
                .add("user_id", payDTO.getUser_id())
                .add("hallid", payDTO.getHallid())
                .add("CHK_ID", payDTO.getCHK_ID())
                .add("user_name", payDTO.getUser_name())
                .add("date", payDTO.getDate())
                .add("Currency", "RMB")
                .add("abamount_limit", "0")
                .add("amount", payDTO.getAmount().toString())
                .add("amount_memo", payDTO.getAmount_memo())
                .add("CommissionCheck", "Y")
                .add("DepositItem", "ARD8")
                .add("ComplexAuditCheck", payDTO.getComplexAuditCheck())
                .add("complex", payDTO.getComplex());
    }

    @Override
    protected IResultHandler<String, Object> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    @Override
    protected ResponseHandler<StanderHttpResponse> getResponseHandler(){
        return HtmlResponseHandler.HTML_RESPONSE_HANDLER;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, Object> {
        private static final Pattern PATTERN = Pattern.compile("alert\\(([\\s\\S]*)\\)");
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, Object> shr) {
            String result = shr.getOriginalEntity();
            log.info("打款响应html：{}", result);
            Matcher matcher = PATTERN.matcher(result);
            if (matcher.find()) {
                String reason = matcher.group(1);
                return Response.FAIL(reason);
            }
            return Response.SUCCESS();
        }
    }
}
