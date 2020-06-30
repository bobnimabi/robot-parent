package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.ao.JuQueryDetailAO;
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

import java.util.ArrayList;

/**
 * Created by tanke on 11/15/2019 12:29 PM
 * XBB局查层数(消消除)
 */
@Slf4j
@Service
public class XBBJuQueryDetailFunction extends AbstractFunction<JuQueryDetailAO,String,Integer> {


     @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.XBB_JU_QUERY_DETAIL;

    }

    // TODO 这里的AO也是,XBB电子的局查询细节是一个独立的FUNtion,这里也应该有xbb的局查询细节的AO
     @Override
    protected IEntity getEntity(JuQueryDetailAO xQueryDTO, RobotWrapper robotWrapper) {



        return UrlEntity.custom(1)
                .add("token",xQueryDTO.getToken());

    }


    @Override
    protected IResultHandler<String, Integer> getResultHandler() {
        return ResultHandler.INSTANCE;
    }



    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,Integer> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}


       /**
         * 获取消除层数
         * @param srp
         * @return
         */
        @Override
        public Response parse2Obj(StanderHttpResponse<String,Integer> srp) {

            String result = srp.getOriginalEntity();

            JSONObject jsonObject = JSON.parseObject(result);
            String data = jsonObject.getString("data");
            JSONObject json = JSON.parseObject(data);
            JSONArray lines = json.getJSONArray("lines");
            int length=0;
            for (Object line : lines) {
                JSONObject jt = JSON.parseObject(line.toString());
                String texts = jt.getString("text");
                if (org.apache.commons.lang3.StringUtils.isNotBlank(texts)){
                    length++;
                }

               if(length>=2){
                   return Response.SUCCESS(length-1);
               }
            }


            return Response.FAIL("未查询到消除层数");
        }

    }
}



