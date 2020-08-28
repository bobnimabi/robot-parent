package com.robot.bbin.base.function;

import com.robot.bbin.base.ao.XBBGetGameCodeAO;
import com.robot.bbin.base.ao.XBBTotalBetGameAO;
import com.robot.bbin.base.basic.PathEnum;
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
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 根据单号和userid查询游戏名称
 */
@Slf4j
@Service
public class XBBGetGameCodeFunction extends AbstractFunction<XBBGetGameCodeAO,String,String> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.GET_GAMECODE;
    }


    @Override
    protected IEntity getEntity(XBBGetGameCodeAO gameDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(7)
                .add("SearchData", "MemberBets")
                .add("BarID", "4")
                .add("GameKind", "76")
                .add("Wagersid", gameDTO.getOrderNo())
                .add("Limit", "50")
                .add("UserID", gameDTO.getUserID());


    }

    @Override
    protected String getUrlSuffix(XBBGetGameCodeAO params) {
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
    protected IResultHandler<String, String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,String> {
        private static final ResultHandler INSTANCE = new ResultHandler();

        private ResultHandler() {
        }

        @Override
        public Response parse2Obj(StanderHttpResponse<String, String> shr) {

            String html = shr.getOriginalEntity();

            Document doc = Jsoup.parse(html);

            String gameName = doc.select("tbody>tr>td").get(2).text();

           return Response.SUCCESS(gameName);
        }
    }


}
