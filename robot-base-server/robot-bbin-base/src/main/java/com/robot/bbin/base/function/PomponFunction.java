package com.robot.bbin.base.function;

import com.robot.bbin.base.ao.JuQueryDetailAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
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
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 局查询弹窗(彩球加赠-查询彩球个数)
 */
@Slf4j
@Service
public class PomponFunction extends AbstractFunction<JuQueryDetailAO,String,Integer> {
    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY_DETAIL;
    }

    @Override
    protected IEntity getEntity(JuQueryDetailAO queryDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(5)
                .add("lang", "cn")
                .add("wid", queryDTO.getOrderNo())// 注单编号
                .add("id", queryDTO.getPageId()) // 平台编码
                .add("gametype", queryDTO.getGameType()) // 游戏编码
                .add("key", queryDTO.getKey()) // 页面携带参数
                .add("rounddate", queryDTO.getRounddate());
    }



    @Override
    protected IResultHandler<String, Integer> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,Integer> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String,Integer> srp) {
            String result = srp.getOriginalEntity();
            Document doc = Jsoup.parse(result);
            Element table = doc.select("table").get(1);// :eq(1)
            Elements tbody_trs = table.select("tbody tr");
            int num = 0;
            for (Element tbody_tr : tbody_trs) {
                Elements tds = tbody_tr.getElementsByTag("td");
                if (null != tds) {
                    if (tds.size() >= 2) {
                        Element element = tds.get(1);
                        if (null != element) {
                            String text = element.html();
                            if (!StringUtils.isEmpty(text) && text.contains("id=7")) {
                                ++num;
                            }
                        }
                    }
                }
            }
            if (0 == num) {
                return Response.FAIL("有注单未包含彩球");
            }
            return Response.SUCCESS(num);
        }
    }
}
