package com.robot.bbin.base.function;

import com.robot.bbin.base.basic.PathEnum;
import com.robot.gpk.base.ao.TotalBetGameAO;
import com.robot.bbin.base.bo.TotalBetGameBO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.HtmlResponseHandler;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.apache.http.client.ResponseHandler;
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
 * 查询投注总金额(游戏)
 */
@Service
public class TotalBetGame extends AbstractFunction<TotalBetGameAO,String,List<TotalBetGameBO>> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.TOTAL_BET_BY_GAME;
    }

    /**
     * 注释掉的参数，点击“查询”按钮的时候会全部带上，点击“BB电子”是没有的，为了方便临时先去掉
     *      .add("BarID", gameDTO.getBarId())
     *      .add("GameKind", gameDTO.getGameKind())
     *      .add("GameType", "-1") // -1表示全部
     *      .add("Limit", "100")
     *      .add("Sort", "DESC")
     */
    @Override
    protected ICustomEntity getEntity(TotalBetGameAO gameDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(4)
                .add("SearchData", "MemberBets")
                .add("date_start", gameDTO.getDateStart())
                .add("date_end", gameDTO.getDateEnd())
                .add("UserID", gameDTO.getUserID());
    }

    @Override
    protected IResultHandler<String, List<TotalBetGameBO>> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    @Override
    protected ResponseHandler<StanderHttpResponse> getResponseHandler(){
        return HtmlResponseHandler.HTML_RESPONSE_HANDLER;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,List<TotalBetGameBO>> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, List<TotalBetGameBO>> shr) {
            String result = shr.getOriginalEntity();
            Document document = Jsoup.parse(result);
            Elements table = document.select("table[class=table table-hover text-middle table-bordered]");
            Elements trs = table.get(0).select("tbody tr");
            List<TotalBetGameBO> list = new ArrayList<TotalBetGameBO>();
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                TotalBetGameBO gameVO = new TotalBetGameBO(
                        tds.get(0).text(),
                        Integer.parseInt(tds.get(1).text().replaceAll(",","")),
                        new BigDecimal(tds.get(2).text().replaceAll(",","")),
                        new BigDecimal(tds.get(3).text().replaceAll(",","")));
                list.add(gameVO);
            }
            return Response.SUCCESS(list);
        }
    }
}
