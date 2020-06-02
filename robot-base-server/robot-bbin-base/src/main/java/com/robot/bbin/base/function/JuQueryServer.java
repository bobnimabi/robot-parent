package com.robot.bbin.base.function;

import com.bbin.utils.UrlUtils;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.bo.ResponseBO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.dto.Response;
import com.robot.code.service.ITenantRobotDictService;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Author mrt
 * @Date 2020/6/2 14:58
 * @Version 2.0
 */
public class JuQueryServer extends AbstractFunction<JuQueryAO, String, JuQueryBO> {

    @Autowired
    private ITenantRobotDictService dictService;

    // 字典表：获取注单查询的barID的前缀
    private String DICT_BAR_ID = "BBIN:ORDER_QUERY:";

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY;
    }

    @Override
    protected ICustomEntity getEntity(JuQueryAO juQueryAO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(6)
                .add("SearchData", "BetQuery")
                .add("BarID", juQueryAO.getBarId())
                .add("GameKind", juQueryAO.getGameKind())
                .add("Wagersid", juQueryAO.getOrderNo())
                .add("Limit", "50") // 每页大小
                .add("Sort", "DESC"); // 时间倒排
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
            // tbody为空表示没有
            Elements tds = doc.select("table[class=table table-hover text-middle table-bordered] tbody tr td");
            if (CollectionUtils.isEmpty(tds)) {
                return Response.FAIL("记录不存在");
            }

            // 获取显示值table table-hover text-middle table-bordered
            JuQueryBO juQueryVO = new JuQueryBO();
            juQueryVO.setOrderTime(DateUtils.format(tds.get(0).text()));
            juQueryVO.setPlatFormOrderNo(tds.get(1).text());
            juQueryVO.setGameName(tds.get(2).text());
            juQueryVO.setHall(tds.get(3).text());
            juQueryVO.setUserName(tds.get(4).text());
            juQueryVO.setResult(tds.get(5).text());
            juQueryVO.setRebateAmount(MoneyUtil.formatYuan(tds.get(6).text()));
            juQueryVO.setSendAmount(MoneyUtil.formatYuan(tds.get(7).text()));

            // 获取隐藏值
            String span = tds.get(6).select("span input").val();
            if (!StringUtils.isEmpty(span)) {
                Map<String, String> urlParams = UrlUtils.getUrlParams(span);
                juQueryVO.setPageId(urlParams.get("id"));
                juQueryVO.setKey(urlParams.get("key"));
            }
            return Response.SUCCESS(juQueryVO);
        }
    }
}
