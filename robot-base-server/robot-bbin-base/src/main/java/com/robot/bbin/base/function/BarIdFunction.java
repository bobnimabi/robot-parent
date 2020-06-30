package com.robot.bbin.base.function;


import com.robot.bbin.base.ao.BarIdAO;

import com.robot.bbin.base.basic.PathEnum;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.CustomHeaders;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 局查询前：获取BarId
 * @Author mrt
 * @Date 2020/6/2 14:58
 * @Version 2.0
 */
@Service
public class BarIdFunction extends AbstractFunction<BarIdAO, String, Map<String, String>> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.BAR_ID;
    }

    @Override
    protected IEntity getEntity(BarIdAO barIdAO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(4)
                .add("SearchData", barIdAO.getSearchData())
                .add("GameKind", barIdAO.getGameKind())
                .add("date_start", barIdAO.getDate_start())
                .add("date_end", barIdAO.getDate_end());
    }

    @Override
    protected String getUrlSuffix(BarIdAO params) {
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
    protected IResultHandler<String, Map<String, String>> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, Map<String, String>> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private static final Pattern PATTERN = Pattern.compile("var BarID = '([\\w-]*)'");
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, Map<String, String>> shr) {

            String result = shr.getOriginalEntity();
            Document document = Jsoup.parse(result);
            Element selects = document.getElementById("functionselects");
            Elements options = selects.children();
            Map<String, String> map = new HashMap<>(4);
            for (Element option : options) {
                map.put(option.text(), option.val());
            }
            if (CollectionUtils.isEmpty(map)) {
                return Response.FAIL("获取BarID失败");
            }
            return Response.SUCCESS(map);
        }

    }
}
