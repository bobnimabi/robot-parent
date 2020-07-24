package com.robot.gpk.base.function;


import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.gpk.base.ao.TotalBetGameAO;
import com.robot.gpk.base.basic.PathEnum;
import com.robot.gpk.base.bo.TotalBetGameBO;
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
public class TotalBetGameFunction extends AbstractFunction<TotalBetGameAO,String, List<TotalBetGameBO>> {

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
    protected IEntity getEntity(TotalBetGameAO gameDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(4)
                .add("SearchData", gameDTO.getSearchData())
                .add("date_start", gameDTO.getDateStart())
                .add("date_end", gameDTO.getDateEnd())
                .add("UserID", gameDTO.getUserID());
    }

    @Override
    protected String getUrlSuffix(TotalBetGameAO params) {
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
    protected IResultHandler<String, List<TotalBetGameBO>> getResultHandler() {
        return ResultHandler.INSTANCE;
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
