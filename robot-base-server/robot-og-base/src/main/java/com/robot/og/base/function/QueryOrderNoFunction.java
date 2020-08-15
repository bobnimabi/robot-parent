package com.robot.og.base.function;


import com.robot.center.util.DateUtils;
import com.bbin.utils.UrlUtils;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryOrderNoAO;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.QueryBetBO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询主单号
 */
@Slf4j
@Service
public class QueryOrderNoFunction extends AbstractFunction<QueryOrderNoAO,String, QueryBetBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.QUERY_ODERNO;
    }

    @Override
    protected IEntity getEntity(QueryOrderNoAO ao, RobotWrapper robotWrapper) {
        return UrlEntity.custom(1)
                .add("type", "queryMemberReportDetail")
                .add("accountId", ao.getAccountId())
                .add("platform", ao.getPlatform())
                .add("startDate", DateUtils.format(ao.getStartDate()))
                .add("lastDate", DateUtils.format(ao.getLastDate()))
                .add("bettingCode", UrlUtils.getURLEncoderString(ao.getBettingCode()))
                ;
    }

    @Override
    protected IResultHandler<String, QueryBetBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：
     * 存在返回：
     */
    private static final class ResultHandler implements IResultHandler<String, QueryBetBO>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, QueryBetBO> shr) throws Exception {
            String result = shr.getOriginalEntity();
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("查询主单号功能未响应");
            }
            Document doc = Jsoup.parse(result);

            Element table = doc.select("body table").get(0);

            Elements td = table.select("tbody tr td");
            if (td.size()==0){
                return Response.FAIL("注单号有误");
            }

            QueryBetBO queryBetBO = new QueryBetBO();

            queryBetBO.setPlatFormOrderNo(td.get(1).text());
            queryBetBO.setRebateAmount(td.get(9).text());
            queryBetBO.setUserName(td.get(4).text());

            //解析获取时间
            Elements align = doc.getElementsByAttribute("align");

            Elements allElements = align.get(1).getAllElements();

            String oderTime = allElements.get(5).text();
            String time =DateUtils.format(DateUtils.gmtStrToTime(oderTime));

            queryBetBO.setOrderTime(time);
           return Response.SUCCESS(queryBetBO);
        }
    }

}
