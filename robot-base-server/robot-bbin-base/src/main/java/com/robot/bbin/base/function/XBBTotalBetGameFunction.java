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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 查询投注总金额(每种游戏在一段时间内的投注总额)
 */
@Slf4j
@Service
public class XBBTotalBetGameFunction extends AbstractFunction<XBBTotalBetGameAO,String,List<XBBTotalBetGameBO>> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.TOTAL_BET_BY_GAME;
    }

    /**
     * 注释掉的参数，点击“查询”按钮的时候会全部带上，点击“BB电子”是没有的，为了方便临时先去掉
     * .add("BarID", gameDTO.getBarId())
     * .add("GameKind", gameDTO.getGameKind())
     * .add("GameType", "-1") // -1表示全部
     * .add("Limit", "100")
     * .add("Sort", "DESC")
     */
    @Override
    protected IEntity getEntity(XBBTotalBetGameAO gameDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(4)
                .add("SearchData", gameDTO.getSearchData())
                .add("BarID", gameDTO.getBarId())
                .add("date_start", gameDTO.getDateStart())
                .add("date_end", gameDTO.getDateEnd())
                .add("UserID", gameDTO.getUserID());

//                .add("gameKind",gameDTO.getGameKind())

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
        return CustomHeaders.custom(4).add("X-Requested-With", "");
    }

    @Override
    protected IResultHandler<String, List<XBBTotalBetGameBO>> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, List<XBBTotalBetGameBO>> {
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, List<XBBTotalBetGameBO>> shr) {

            String html = shr.getOriginalEntity();
            Document document = Jsoup.parse(html);
            List<XBBTotalBetGameBO> totalbetBo = new ArrayList<>();
            Elements bg999 = document.getElementsByClass("bg-999");

            Elements th = bg999.select("th");

            Elements tbody = document.getElementsByTag("tbody");
            Elements td = tbody.select("td");
           /* String element2 = td.get(2).text();
            System.out.println("element2 = " + element2);*/


            for (int i = 0; i < th.size(); i++) {

                XBBTotalBetGameBO gameVO = new XBBTotalBetGameBO(
                        td.get(2).text(),
                        Integer.parseInt(th.get(1).text().replaceAll(",", "")),
                        new BigDecimal(th.get(11).text().replaceAll(",", "")),
                        new BigDecimal(th.get(4).text().replaceAll(",", ""))

                );
                // System.out.println("ths  " +i+ th.get(11).text());
                totalbetBo.add(gameVO);
            }


            return Response.SUCCESS(totalbetBo);
        }
    }


}
