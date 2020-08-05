package com.robot.bbin.base.function;

import com.bbin.utils.UrlUtils;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.code.service.ITenantRobotDictService;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class JuQueryFunction extends AbstractFunction<JuQueryAO, String, JuQueryBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY;
    }

    @Override
    protected IEntity getEntity(JuQueryAO juQueryAO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(6)
                .add("SearchData", juQueryAO.getSearchData())
                .add("BarID", juQueryAO.getBarId())
                .add("GameKind", juQueryAO.getGameKind())
                .add("Wagersid", juQueryAO.getOrderNo())
                .add("Limit", juQueryAO.getLimit())
                .add("Sort", juQueryAO.getSort());
    }

    @Override
    protected String getUrlSuffix(JuQueryAO params) {
        return params.getGameKind();

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



