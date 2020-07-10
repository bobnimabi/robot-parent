package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.ao.JuQueryDetailAO;
import com.robot.bbin.base.ao.XBBJuQueryDetailAO;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.code.response.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.IEntity;
import com.robot.core.http.request.JsonEntity;
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
public class XBBJuQueryDetailFunction extends AbstractFunction<XBBJuQueryDetailAO,String,Integer> {


     @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.XBB_REQUERY_DETAIL;

    }

    // TODO 这里的AO也是,XBB电子的局查询细节是一个独立的FUNtion,这里也应该有xbb的局查询细节的AO
     @Override
    protected IEntity getEntity(XBBJuQueryDetailAO xQueryDTO, RobotWrapper robotWrapper) {



        return JsonEntity.custom(1)
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
            }
            if(length>=2){
                return Response.SUCCESS(length-1);
            }

            return Response.FAIL("未查询到消除层数");
        }

    }

    public static void main(String[] args) {
            String result="{\"error_code\":\"0\",\"error_text\":\"\",\"data\":{\"wager_id\":194706855,\"bet_time_iso\":\"2020-07-08T04:29:04+08:00\",\"rate\":\"1:10\",\"bet_each_amount\":\"3.00\",\"bet_each_credit\":\"30\",\"bet_level\":1,\"bet_amount\":\"0.00\",\"bet_credit\":\"0\",\"line_count\":0,\"currency_code\":\"CNY\",\"game_id\":1100006,\"game_name\":\"糖果派对\",\"game_type\":\"crush_continue\",\"current_level\":2,\"brick_num\":12,\"brick_threshold\":15,\"has_multiple_card\":false,\"cards\":[[[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol10\",\"symbol6\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\",\"symbol7\"],[\"symbol10\",\"symbol9\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol9\",\"symbol9\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol9\",\"symbol8\",\"symbol8\"]],[[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol7\",\"symbol6\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol9\",\"symbol7\"],[\"symbol10\",\"symbol9\",\"symbol7\",\"symbol6\",\"symbol9\"],[\"symbol9\",\"symbol9\",\"symbol9\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol9\",\"symbol8\",\"symbol8\"]],[[\"symbol8\",\"symbol8\",\"symbol8\",\"symbol7\",\"symbol6\"],[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol9\",\"symbol7\"],[\"symbol10\",\"symbol8\",\"symbol9\",\"symbol6\",\"symbol9\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol7\",\"symbol8\",\"symbol8\"]],[[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol7\",\"symbol6\"],[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol9\",\"symbol7\"],[\"symbol10\",\"symbol9\",\"symbol9\",\"symbol6\",\"symbol9\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol7\",\"symbol8\",\"symbol8\"]]],\"hit_symbol\":\"\",\"result_symbol\":\"\",\"rewards\":[],\"lines\":[{\"type\":4,\"card\":[[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol10\",\"symbol6\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\",\"symbol7\"],[\"symbol10\",\"symbol9\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol9\",\"symbol9\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol9\",\"symbol8\",\"symbol8\"]],\"text\":\"1\",\"symbols\":[{\"symbol\":\"symbol10\",\"count\":7,\"text\":\"\"}],\"double_time\":\"\",\"payoff_amount\":\"1.50\",\"payoff_credit\":\"15\"},{\"type\":4,\"card\":[[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol7\",\"symbol6\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol9\",\"symbol7\"],[\"symbol10\",\"symbol9\",\"symbol7\",\"symbol6\",\"symbol9\"],[\"symbol9\",\"symbol9\",\"symbol9\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol9\",\"symbol8\",\"symbol8\"]],\"text\":\"2\",\"symbols\":[{\"symbol\":\"symbol9\",\"count\":5,\"text\":\"\"}],\"double_time\":\"\",\"payoff_amount\":\"1.20\",\"payoff_credit\":\"12\"},{\"type\":4,\"card\":[[\"symbol8\",\"symbol8\",\"symbol8\",\"symbol7\",\"symbol6\"],[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol9\",\"symbol7\"],[\"symbol10\",\"symbol8\",\"symbol9\",\"symbol6\",\"symbol9\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol7\",\"symbol8\",\"symbol8\"]],\"text\":\"3\",\"symbols\":[{\"symbol\":\"symbol8\",\"count\":5,\"text\":\"\"}],\"double_time\":\"\",\"payoff_amount\":\"1.50\",\"payoff_credit\":\"15\"},{\"type\":4,\"card\":[[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol7\",\"symbol6\"],[\"symbol6\",\"symbol8\",\"symbol9\",\"symbol9\",\"symbol7\"],[\"symbol10\",\"symbol9\",\"symbol9\",\"symbol6\",\"symbol9\"],[\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\",\"symbol9\"],[\"symbol7\",\"symbol7\",\"symbol7\",\"symbol8\",\"symbol8\"]],\"text\":\"4\",\"double_time\":\"\",\"payoff_amount\":\"0.00\",\"payoff_credit\":\"0\"}],\"sp_rewards\":null,\"sub_total_amount\":\"4.20\",\"sub_total_credit\":\"42\",\"double_time\":\"X2\",\"total_amount\":\"8.40\",\"total_bet_amount\":\"0.00\",\"total_payout_amount\":\"8.40\",\"total_credit\":\"84\",\"total_bet_credit\":\"0\",\"total_payout_credit\":\"84\",\"jackpot\":\"\",\"resource\":{\"symbol10\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol10.png?1586475411\",\"symbol6\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol6.png?1586475411\",\"symbol7\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol7.png?1586475411\",\"symbol8\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol8.png?1586475411\",\"symbol9\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol9.png?1586475411\"},\"is_refund\":false}}";


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


        }
        if(length>=2){
            System.out.println("texts = " + (length-1));
        }


    }
}



