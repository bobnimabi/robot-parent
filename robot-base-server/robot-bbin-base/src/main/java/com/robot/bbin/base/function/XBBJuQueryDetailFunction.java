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
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * XBB局查询弹窗(消消除)
 */
@Slf4j
@Service
public class XBBJuQueryDetailFunction extends AbstractFunction<XBBJuQueryDetailAO,String,Integer> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.XBB_JU_QUERY_DETAIL;
    }

    @Override
    protected IEntity getEntity(XBBJuQueryDetailAO xQueryDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(4)
                .add("gamekind", "76")
                .add("userid", xQueryDTO.getUserid())// 注单编号
                .add("wagersid", xQueryDTO.getWagersid()) // 平台编码
                .add("SearchData", " BetQuery") ;// 游戏编码

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

        /**
         * 测试解析响应层数
         * @param args
         */
       /* public static void main(String[] args) {
            String jsonResult=("{\"error_code\":\"0\",\"error_text\":\"\",\"data\":{\"wager_id\":187402662,\"bet_time_iso\":\"2020-06-23T15:44:17+08:00\",\"rate\":\"1:10\",\"bet_each_amount\":\"1.00\",\"bet_each_credit\":\"10\",\"bet_level\":1,\"bet_amount\":\"1.00\",\"bet_credit\":\"10\",\"line_count\":0,\"currency_code\":\"CNY\",\"game_id\":1100006,\"game_name\":\"糖果派对\",\"game_type\":\"crush_continue\",\"current_level\":2,\"brick_num\":5,\"brick_threshold\":15,\"has_multiple_card\":false,\"cards\":[[[\"symbol9\",\"symbol10\",\"symbol9\",\"symbol9\",\"symbol10\"],[\"symbol10\",\"symbol7\",\"symbol9\",\"symbol9\",\"symbol10\"],[\"symbol9\",\"symbol9\",\"symbol9\",\"symbol10\",\"symbol10\"],[\"symbol6\",\"symbol10\",\"symbol9\",\"symbol8\",\"symbol9\"],[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\"]],[[\"symbol7\",\"symbol9\",\"symbol7\",\"symbol10\",\"symbol10\"],[\"symbol9\",\"symbol10\",\"symbol10\",\"symbol8\",\"symbol10\"],[\"symbol10\",\"symbol7\",\"symbol6\",\"symbol10\",\"symbol10\"],[\"symbol6\",\"symbol10\",\"symbol8\",\"symbol8\",\"symbol9\"],[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\"]],[[\"symbol7\",\"symbol9\",\"symbol7\",\"symbol9\",\"symbol7\"],[\"symbol9\",\"symbol10\",\"symbol10\",\"symbol10\",\"symbol10\"],[\"symbol10\",\"symbol7\",\"symbol6\",\"symbol8\",\"symbol8\"],[\"symbol6\",\"symbol10\",\"symbol8\",\"symbol8\",\"symbol9\"],[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\"]]],\"hit_symbol\":\"\",\"result_symbol\":\"\",\"rewards\":[],\"lines\":[{\"type\":4,\"card\":[[\"symbol9\",\"symbol10\",\"symbol9\",\"symbol9\",\"symbol10\"],[\"symbol10\",\"symbol7\",\"symbol9\",\"symbol9\",\"symbol10\"],[\"symbol9\",\"symbol9\",\"symbol9\",\"symbol10\",\"symbol10\"],[\"symbol6\",\"symbol10\",\"symbol9\",\"symbol8\",\"symbol9\"],[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\"]],\"text\":\"1\",\"symbols\":[{\"symbol\":\"symbol9\",\"count\":8,\"text\":\"\"}],\"double_time\":\"\",\"payoff_amount\":\"2.00\",\"payoff_credit\":\"20\"},{\"type\":4,\"card\":[[\"symbol7\",\"symbol9\",\"symbol7\",\"symbol10\",\"symbol10\"],[\"symbol9\",\"symbol10\",\"symbol10\",\"symbol8\",\"symbol10\"],[\"symbol10\",\"symbol7\",\"symbol6\",\"symbol10\",\"symbol10\"],[\"symbol6\",\"symbol10\",\"symbol8\",\"symbol8\",\"symbol9\"],[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\"]],\"text\":\"2\",\"symbols\":[{\"symbol\":\"symbol10\",\"count\":5,\"text\":\"\"}],\"double_time\":\"\",\"payoff_amount\":\"0.20\",\"payoff_credit\":\"2\"},{\"type\":4,\"card\":[[\"symbol7\",\"symbol9\",\"symbol7\",\"symbol9\",\"symbol7\"],[\"symbol9\",\"symbol10\",\"symbol10\",\"symbol10\",\"symbol10\"],[\"symbol10\",\"symbol7\",\"symbol6\",\"symbol8\",\"symbol8\"],[\"symbol6\",\"symbol10\",\"symbol8\",\"symbol8\",\"symbol9\"],[\"symbol9\",\"symbol10\",\"symbol7\",\"symbol10\",\"symbol10\"]],\"text\":\"3\",\"double_time\":\"\",\"payoff_amount\":\"0.00\",\"payoff_credit\":\"0\"}],\"sp_rewards\":null,\"sub_total_amount\":\"2.20\",\"sub_total_credit\":\"22\",\"double_time\":\"\",\"total_amount\":\"2.20\",\"total_bet_amount\":\"1.00\",\"total_payout_amount\":\"1.20\",\"total_credit\":\"22\",\"total_bet_credit\":\"10\",\"total_payout_credit\":\"12\",\"jackpot\":\"\",\"resource\":{\"symbol10\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol10.png?1586475411\",\"symbol6\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol6.png?1586475411\",\"symbol7\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol7.png?1586475411\",\"symbol8\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol8.png?1586475411\",\"symbol9\":\"https://slot.cfvn66.com/game/1100006/wager/symbol/symbol9.png?1586475411\"},\"is_refund\":false}}");

            JSONObject jsonObject = JSON.parseObject(jsonResult);
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
            System.out.println("length = " + (length-1));



        }*/
    }
}



