package com.robot.og.base.function;
import com.bbin.common.client.BetQueryDto;
import com.bbin.common.dto.robot.BreakThroughDTO;
import com.bbin.common.dto.robot.OGBreakThroughDTO;
import com.bbin.common.util.DateUtils;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanke on 11/14/2019 8:06 PM
 * 下注详情
 */
@Slf4j
@Service
public class GetBetDetailFunction extends AbstractFunction<OGBreakThroughDTO, String, Map<String, Map<String, String>>> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.GET_DETAIL;
    }


    @Override
    protected IEntity getEntity(OGBreakThroughDTO dto, RobotWrapper robotWrapper) {



        //gamelist可以为空
        return  UrlEntity.custom(6)
                .add("account", dto.getUserName())
                .add("startDate",dto.getBeginDate())
                .add("lastDate",dto.getEndDate() )
                .add("plat",dto.getGameCodeList() )

                ;

    }

        @Override
        protected IResultHandler<String, Map<String, Map<String, String>>> getResultHandler () {
            return ResultHandler.INSTANCE;
        }

        /**
         * 响应结果转换：
         * 存在返回：

         */
        private static final class ResultHandler implements IResultHandler<String, Map<String, Map<String, String>>> {
            private static final ResultHandler INSTANCE = new ResultHandler();

            private ResultHandler() {
            }

            @Override
            public Response parse2Obj(StanderHttpResponse<String, Map<String, Map<String, String>>> shr) {
                String result = shr.getOriginalEntity();
                log.info("查询下注详情功能响应：{}");
                if (StringUtils.isEmpty(result)) {
                    return Response.FAIL("未查询到下注记录");
                }
                // GetBetDetailBO betDetailBO = new GetBetDetailBO();
                Map<String, Map<String, String>> mapout = new HashMap<>();
                Document doc = Jsoup.parse(result);
                Elements tables = doc.getElementsByTag("table");
                for (Element table : tables) {
                    Map<String, String> mapinner = new HashMap<>();
                    Elements theadTrThs = table.select("thead tr th");
                    String catagoryStr = theadTrThs.get(0).text();
                    String catagory = catagoryStr.substring(0, catagoryStr.indexOf(" "));
                    String onClickAttr = table.select("tbody tr").get(0).attr("onclick");
                    Elements tbodyTrTds = table.select("tbody tr td");
                    for (int i = 0; i < tbodyTrTds.size(); i++) {
                        mapinner.put(theadTrThs.get(i + 1).text(), tbodyTrTds.get(i).text());
                    }

                    mapout.put(catagory, mapinner);
                }
                if (mapout.size() == 0) {
                    return Response.FAIL("未查询到下注记录");
                }

                return Response.SUCCESS(mapout);


            }

        }


    }



