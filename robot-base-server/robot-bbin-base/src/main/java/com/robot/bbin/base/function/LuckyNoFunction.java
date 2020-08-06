package com.robot.bbin.base.function;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.UrlUtils;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.bo.LuckyNoBO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 局查询：注单查询
 * 局查询页面：
 *      请选择：注单编码
 * @Author mrt
 * @Date 2020/6/2 14:58
 * @Version 2.0
 */
@Slf4j
@Service
public class LuckyNoFunction extends AbstractFunction<OrderNoQueryDTO, String, JuQueryBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY;
    }

    @Override
    protected IEntity getEntity(OrderNoQueryDTO queryDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(6)
                .add("SearchData", "BetQuery")
                .add("BarID", "3")
                .add("GameKind", queryDTO.getGameCode())
                .add("ReferenceID", queryDTO.getOrderNo())
                .add("Limit", "50")
                .add("Sort", "DESC");

    }

    @Override
    protected String getUrlSuffix(OrderNoQueryDTO params) {
        return params.getGameCode();

    }

    /**
     * get不需要携带这个头，故意覆盖掉全局头
     */
    @Override
    protected CustomHeaders getHeaders(RobotWrapper robotWrapper) {
        return CustomHeaders.custom(4).add("X-Requested-With","");
    }

    @Override
    protected IResultHandler<String, JuQueryBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, JuQueryBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, JuQueryBO> shr) {
            String result = shr.getOriginalEntity();

            Document doc = Jsoup.parse(result);
            Elements tds = doc.select("tbody>tr>td");
            if (CollectionUtils.isEmpty(tds)) {
                return Response.FAIL("记录不存在");
            }
            LuckyNoBO bo = new LuckyNoBO();

            bo.setOrderTime(DateUtils.format(tds.get(0).text()));
            bo.setPlatFormOrderNo(tds.get(1).text());
            bo.setGameName(tds.get(2).text());
            bo.setHall(tds.get(3).text());
            bo.setUserName(tds.get(4).text());
            bo.setResult(tds.get(5).text());
            bo.setRebateAmount(MoneyUtil.formatYuan(tds.get(6).text()));
            bo.setSendAmount(MoneyUtil.formatYuan(tds.get(7).text()));
            bo.setThirdPlatNo(tds.get(8).text());

            return Response.SUCCESS(bo);
        }
    }

}



