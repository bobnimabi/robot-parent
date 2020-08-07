package com.robot.og.base.function;

import com.alibaba.fastjson.JSON;

import com.bbin.utils.UrlUtils;
import com.bbin.utils.project.DateUtils;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryOrderNoAO;
import com.robot.og.base.basic.PathEnum;
import com.robot.og.base.bo.QueryBetBO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        public Response parse2Obj(StanderHttpResponse<String, QueryBetBO> shr) {
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
            queryBetBO.setOrderTime(td.get(2).text());
            queryBetBO.setPlatFormOrderNo(td.get(1).text());
            queryBetBO.setRebateAmount(td.get(9).text());
            queryBetBO.setUserName(td.get(4).text());

           return Response.SUCCESS(queryBetBO);

        }
    }

 /*public static void main(String[] args) throws IOException {

        Document doc= Jsoup.parse(new File("C:\\Users\\8888\\IdeaProjects\\robot-parent\\robot-business-server\\robot-og-activity-server\\src\\main\\resources\\test.html"), "utf-8");

     Element table = doc.select("body table").get(0);

     Elements ths = table.select("thead tr th");
     Elements td = table.select("tbody tr td");
        if(td.size()  ==0){
            System.out.println("注单号有误");
        }
     System.out.println("td = " + td);
     String text = td.get(1).text();
     System.out.println("text = " + text);

     QueryBetBO queryBetBO = new QueryBetBO();
     queryBetBO.setOrderTime(td.get(2).text());
     queryBetBO.setPlatFormOrderNo(td.get(1).text());
     queryBetBO.setRebateAmount(td.get(9).text());
     queryBetBO.setUserName(td.get(4).text());

     System.out.println(queryBetBO);
 }*/
}
