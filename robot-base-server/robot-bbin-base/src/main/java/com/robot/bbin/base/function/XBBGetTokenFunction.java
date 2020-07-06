package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.ao.TokenAO;
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
public class XBBGetTokenFunction extends AbstractFunction<TokenAO, String, String> {

    //获取token路径和bb电子路径相同
    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.XBB_GETTOKEN;
    }

    @Override
    protected IEntity getEntity(TokenAO xQueryDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(4)
                          .add("userid",xQueryDTO.getUserid())
                          .add("wagersid", xQueryDTO.getWagersid())
                          .add("SearchData", "BetQuery") ;

    }


    @Override
    protected String getUrlSuffix(TokenAO params) {
        return params.getGamekind();
    }



    @Override
    protected IResultHandler<String,String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }



    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,String> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        /**
         * 获取跳转后页面新token
         * @param srp
         * @return
         */

        @Override
        public Response parse2Obj(StanderHttpResponse<String,String> srp) {

            String result = srp.getOriginalEntity();

            JSONObject jsonObject = JSON.parseObject(result);
            String data = jsonObject.getString("data");

            Pattern P= Pattern.compile("token=(.*?)&");
            Matcher m=P.matcher(data);
            String tokens=null;
            while (m.find()) {
                String token = m.group(1);


                if(null!=token){
                    tokens=token;

                    return Response.SUCCESS(tokens);
                }
            }


            return Response.FAIL("获取XBB token失败");
        }


    }
}



