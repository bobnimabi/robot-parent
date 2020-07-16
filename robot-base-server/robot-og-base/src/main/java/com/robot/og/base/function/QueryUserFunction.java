package com.robot.og.base.function;

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
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 查询用户是否存在，和基本信息
 */
@Slf4j
@Service
public class QueryUserFunction extends AbstractFunction<String,String, String> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.QUERY_USER;
    }

    @Override
    protected IEntity getEntity(String account, RobotWrapper robotWrapper) {
        return UrlEntity.custom(1)
                .add("type", "queryManDeposit")
                .add("account", account)
                ;
    }

    @Override
    protected IResultHandler<String, String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换：
     *
     */
    private static final class ResultHandler implements IResultHandler<String, String>{
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, String> shr) {
            String result = shr.getOriginalEntity();
            log.info("查询会员存在功能响应：{}", result);

            Document doc = Jsoup.parse(result);
            String account = doc.getElementsByTag("table").get(0).select("input").attr("value");


            if (null == account) {
                return Response.FAIL("会员账号不存在");
            }
            return Response.SUCCESS("会员账号存在");



                
        }
    }



//测试解析
   /* public static void main(String[] args) throws IOException {

            Document document = Jsoup.parse(new File("E:\\project\\robot-parent\\robot-business-server\\robot-og-activity-server\\src\\main\\resources\\fales.html"), "utf-8");

        String table = document.getElementsByTag("table").get(0).select("input").attr("value");
        System.out.println("table = " + table);


    }*/





}
