package com.robot.bbin.base.function;

import com.robot.gpk.base.ao.JuQueryDetailAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 局查询弹窗(消消除)
 */
@Service
public class JuQueryDetailServer extends AbstractFunction<JuQueryDetailAO,String,Integer> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY_DETAIL;
    }

    @Override
    protected ICustomEntity getEntity(JuQueryDetailAO queryDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(5)
                .add("lang", "cn")
                .add("wid", queryDTO.getOrderNo())// 注单编号
                .add("id", queryDTO.getPageId()) // 平台编码
                .add("gametype", queryDTO.getGameType()) // 游戏编码
                .add("key", queryDTO.getKey()); // 页面携带参数
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
            for (Element tbody_tr : tbody_trs) {
                Elements tds = tbody_tr.getElementsByTag("td");
                if (tds.size() >= 2) {
                    Element td1 = tbody_tr.getElementsByTag("td").get(0);
                    System.out.println(td1);
                    Element td2 = tbody_tr.getElementsByTag("td").get(1);
                    if ("-".equals(td2.text())) {
                        return Response.SUCCESS(Integer.parseInt(td1.text()) - 1);
                    }
                }
            }
            return Response.FAIL("未查询到消除层数");
        }
    }
}
