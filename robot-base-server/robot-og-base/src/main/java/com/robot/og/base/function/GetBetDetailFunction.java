package com.robot.og.base.function;
import com.robot.center.util.DateUtil;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.GetBetDetailAO;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.GetBetDetailBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanke on 11/14/2019 8:06 PM
 * 下注详情
 */
@Slf4j
@Service
public class GetBetDetailFunction extends AbstractFunction<GetBetDetailAO,String, GetBetDetailBO> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.GET_DETAIL;
    }

    //todo  参数
    @Override
    protected IEntity getEntity(GetBetDetailAO ao, RobotWrapper robotWrapper) {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        return UrlEntity.custom(50)
                .add("account", ao.getAccount())
                .add("startDate", DateUtil.YEAR_MONTH_DAY_MORE.format(ao.getStartDate()))
                .add("endDate", DateUtil.YEAR_MONTH_DAY_MORE.format(ao.getEndDate()))
                .add("gameCode", ao.getGameCode())
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                .add("gameid", "")
                ;


    }

    @Override
    protected IResultHandler<String, GetBetDetailBO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：
     * 存在返回：
     *      {"code":0,"IsSuccess":true,}
     * 不存在返回：
     *      {"code":1,"IsSuccess":false,}
     */
    private static final class ResultHandler implements IResultHandler<String, GetBetDetailBO>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, GetBetDetailBO> shr) {
            String result = shr.getOriginalEntity();
            log.info("查询下注详情功能响应：{}", result);
            if (StringUtils.isEmpty(result)) {
                return Response.FAIL("查询下注未响应");
            }

          /*  QueryUserResultBO usesrResultVO = JSON.parseObject(result, QueryUserResultBO.class);
            if (null == usesrResultVO.getCode()) {
                return Response.FAIL("转换失败");
            }
            return Response.SUCCESS(usesrResultVO);*/

            Map<String, Map<String, String>> mapout = new HashMap<>();
            Document doc = Jsoup.parse(result);
            Elements tables = doc.getElementsByTag("table");
            for (Element table: tables) {
                Map<String, String> mapinner = new HashMap<>();
                Elements theadTrThs = table.select("thead tr th");
                String catagoryStr = theadTrThs.get(0).text();
                String catagory = catagoryStr.substring(0, catagoryStr.indexOf(" "));
                String onClickAttr = table.select("tbody tr").get(0).attr("onclick");
                Elements tbodyTrTds = table.select("tbody tr td");
                for (int i = 0; i < tbodyTrTds.size(); i++) {
                    mapinner.put(theadTrThs.get(i + 1).text(),tbodyTrTds.get(i).text());
                }
                mapout.put(catagory, mapinner);
            }

            return Response.SUCCESS(mapout);



                
        }

    }

}
