package com.robot.bbin.base.function;

import com.bbin.utils.UrlUtils;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.BarIdAO;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.center.util.MoneyUtil;
import com.robot.code.response.Response;
import com.robot.code.service.ITenantRobotDictService;
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
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 局查询前：获取BarId
 * @Author mrt
 * @Date 2020/6/2 14:58
 * @Version 2.0
 */
@Service
public class BarIdFunction extends AbstractFunction<BarIdAO, String, String> {

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
    protected IResultHandler<String, String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String, String> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private static final Pattern PATTERN = Pattern.compile("var BarID = '([\\w-]*)'");
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, String> shr) {
            String result = shr.getOriginalEntity();
            Matcher matcher = PATTERN.matcher(result);
            if (matcher.find()) {
                String barID = matcher.group(1);///group(0)
                return Response.SUCCESS(barID);
            }
            return Response.FAIL("获取BarID失败");
        }
    }
}
