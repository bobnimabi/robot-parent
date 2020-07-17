package com.robot.og.base.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.pojo.CashDetailListVO;
import com.bbin.common.pojo.CashDetailVO;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import com.robot.og.base.ao.QueryRechargeAO;
import com.robot.og.base.basic.PathEnum;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户是否存在，和基本信息
 */
@Slf4j
@Service
public class QueryUserRecordFunction extends AbstractFunction<QueryRechargeAO,String, String> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.QUERY_USER;
    }

    @Override
    protected IEntity getEntity(QueryRechargeAO ao, RobotWrapper robotWrapper) {
        return UrlEntity.custom(1)
                .add("type", "queryRecord")
                .add("tradeTypes", "")
                .add("isPostback", "1")
                .add("orderField", "createDateTime")
                .add("sortBy", "DESC")
                .add("selDate", "0")
                .add("startDate", ao.getStartDate())
                .add("endDate", ao.getEndDate())
                .add("actType", "0")
                .add("memberNo", ao.getMemberNo()) //usename
                .add("pageSize", "20")
                ;
    }

    @Override
    protected IResultHandler<String, String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：

     */
    private static final class ResultHandler implements IResultHandler<String, String>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, String> shr) {
            String result = shr.getOriginalEntity();
            log.info("查询会员存在功能响应：{}", result);
            Document document = Jsoup.parse(result);
            Elements trs = document.select("tr[onMouseMove=javascript:this.bgColor='#CLEBFF';][onMouseOut=javascript:this.bgColor='#FFFFFF';]");
            List<CashDetailVO> list = new ArrayList<>(20);
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                CashDetailVO cashDetailVO = new CashDetailVO();
                cashDetailVO.setUserName(tds.get(1).text());
                cashDetailVO.setTradeType(tds.get(2).text());
                cashDetailVO.setAmount(new BigDecimal(tds.get(3).text()));
                cashDetailVO.setBalance(new BigDecimal(tds.get(4).text()));
                cashDetailVO.setTradeDate(tds.get(5).text());
                cashDetailVO.setOrderNo(tds.get(6).text());
                list.add(cashDetailVO);
            }
            int totalCount = Integer.parseInt(document.select("span[name=totalCount]").get(0).val());

            CashDetailListVO cashDetailListVO = new CashDetailListVO();
            cashDetailListVO.setList(list);
            cashDetailListVO.setMaxPage((totalCount + 20 + 1) / 20);
            return Response.SUCCESS(cashDetailListVO);


                
        }
    }

}
