package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.ao.XBBJuQueryAO;
import com.robot.bbin.base.ao.XBBJuQueryDetailAO;
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
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * XBB获取新token
 */
@Slf4j
@Service
public class XBBGetTokenFunction extends AbstractFunction<XBBJuQueryAO, String, XBBJuQueryDetailAO> {

    //获取token路径和bb电子路径相同
    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY_DETAIL;
    }

    @Override
    protected IEntity getEntity(XBBJuQueryAO xQueryDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(4)
                          .add("gamekind", "76")
                          .add("userid", xQueryDTO.getUserid())// 注单编号
                          .add("wagersid", xQueryDTO.getWagersid()) // 平台编码
                          .add("SearchData", " BetQuery") ;// 游戏编码

    }


    @Override
    protected String getUrlSuffix(XBBJuQueryAO params) {
        return params.getGamekind();
    }



    @Override
    protected IResultHandler<String, XBBJuQueryDetailAO> getResultHandler() {
        return ResultHandler.INSTANCE;
    }



    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,XBBJuQueryDetailAO> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        /**
         * 获取跳转后页面新token
         * @param srp
         * @return
         */

        @Override
        public Response parse2Obj(StanderHttpResponse<String,XBBJuQueryDetailAO> srp) {

            String result = srp.getOriginalEntity();

            JSONObject jsonObject = JSON.parseObject(result);
            String data = jsonObject.getString("data");

            Pattern P= Pattern.compile("token=(.*?)&");
            Matcher m=P.matcher(data);
            while (m.find()) {
                String token = m.group(1);


                if(null!=token){
                    XBBJuQueryDetailAO xbb = new XBBJuQueryDetailAO();
                    xbb.setToken(token);

                    return Response.SUCCESS(xbb);
                }
            }


            return Response.FAIL("获取XBB token失败");
        }




        /**
         * 测试解析响应token
         * @param args
         */
      /*  public static void main(String[] args) {


        String result=("{\"error_code\":0,\"error_message\":null,\"execution_time\":\"308 ms\",\"server_name\":\"outside9-backend.pid.prod\",\"data\":\"https:\\/\\/ex.xbb-slot.com\\/wager-detail\\/#\\/externalWagerDetail\\/?token=5663033b07f7048f0e3da4a033ac4a31&lang=zh-cn\",\"trace_id\":\"5ef5a28d42587\"}");

            JSONObject jsonObject = JSON.parseObject(result);
            String data = jsonObject.getString("data");

            Pattern P= Pattern.compile("token=(.*?)&");
            Matcher m=P.matcher(data);
            while (m.find()) {
                System.out.println(m.group(1));
            }


        }*/
    }
}



