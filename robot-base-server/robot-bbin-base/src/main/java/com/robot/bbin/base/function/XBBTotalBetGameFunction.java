package com.robot.bbin.base.function;

import com.robot.bbin.base.ao.TotalBetGameAO;
import com.robot.bbin.base.ao.XBBTotalBetGameAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.TotalBetGameBO;
import com.robot.bbin.base.bo.XBBTotalBetGameBO;
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

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 查询投注总金额(每种游戏在一段时间内的投注总额)
 */
@Slf4j
@Service
public class XBBTotalBetGameFunction extends AbstractFunction<XBBTotalBetGameAO,String,XBBTotalBetGameBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.TOTAL_BET_BY_GAME;
    }


    @Override
    protected IEntity getEntity(XBBTotalBetGameAO gameDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(7)
                .add("SearchData", gameDTO.getSearchData())
                .add("BarID", gameDTO.getBarId())
                .add("date_start", gameDTO.getDateEnd())    //查询时间为注单号产生时间
                .add("date_end", gameDTO.getDateEnd())
                .add("GameKind", gameDTO.getGameKind())
                .add("GameType", gameDTO.getGameType())
                .add("UserID", gameDTO.getUserID());
    }

    @Override
    protected String getUrlSuffix(XBBTotalBetGameAO params) {
        return params.getGameKind();
    }

    /**
     * get不需要携带这个头，故意覆盖掉全局头
     */
    @Override
    protected CustomHeaders getHeaders(RobotWrapper robotWrapper) {
        return CustomHeaders.custom(1).add("X-Requested-With", "");
    }

    @Override
    protected IResultHandler<String, XBBTotalBetGameBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,XBBTotalBetGameBO> {
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, XBBTotalBetGameBO> shr) {

            String html = shr.getOriginalEntity();

            Document document = Jsoup.parse(html);


            Elements bg999 = document.getElementsByClass("bg-999");

            Elements th = bg999.select("th");

            Elements tbody = document.getElementsByTag("tbody");
            if(null== tbody)
                return Response.FAIL("查询下注总额失败");
            Elements td = tbody.select("td");

                XBBTotalBetGameBO  totalbetBo = new XBBTotalBetGameBO(
                        td.get(2).text(),
                        Integer.parseInt(th.get(1).text().replaceAll(",", "")),
                        new BigDecimal(th.get(11).text().replaceAll(",", "")),
                        new BigDecimal(th.get(4).text().replaceAll(",", ""))

                );
                return Response.SUCCESS(totalbetBo);

        }
    }

}
