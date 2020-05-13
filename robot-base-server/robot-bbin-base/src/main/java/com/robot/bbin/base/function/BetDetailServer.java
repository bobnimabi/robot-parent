package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bbin.common.response.ResponseResult;
import com.robot.bbin.base.basic.ActionEnum;
import com.robot.bbin.base.dto.BetDTO;
import com.robot.bbin.base.dto.BetDetailDTO;
import com.robot.bbin.base.vo.BetDetailVO;
import com.robot.bbin.base.vo.BetVO;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.httpclient.UrlCustomEntity;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 下注查询
 */
@Service
public class BetDetailServer extends FunctionBase<BetDetailDTO> {

    @Override
    public ResponseResult doFunctionFinal(ParamWrapper<BetDetailDTO> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        BetDetailDTO betDetailDTO = paramWrapper.getObj();
        // 执行
        StanderHttpResponse response = execute.request(robotWrapper, CustomHttpMethod.POST_FORM, action, null,
                createParams(betDetailDTO), null, Parse.INSTANCE);
        ResponseResult responseResult = response.getResponseResult();
        return responseResult;
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.BET_ANALYSIS_DETAIL;
    }

    //组装局查询
    private UrlCustomEntity createParams(BetDetailDTO betDTO) throws Exception{
        return UrlCustomEntity.custom()
                .add("listid", betDTO.getListid())
                .add("start", betDTO.getStart())
                .add("end", betDTO.getEnd())
                .add("game", betDTO.getGame())
                .add("currency", betDTO.getCurrency())
                .add("gametype", betDTO.getGametype());
    }

    /**
     * 响应结果转换类
     */
    private static final class Parse implements IResultParse {
        private static final Parse INSTANCE = new Parse();
        private Parse(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未返回任何响应");
            }
            List<BetDetailVO> list = new ArrayList<>(5);
            JSONObject jsonObject = JSON.parseObject(result);
            Collection<Object> values = jsonObject.values();
            for (Object value : values) {
                Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) value;
                map.forEach((gameCode,v)->{
                    Map<String, String> vv = (Map<String, String>) v;
                    String vvString = JSON.toJSONString(vv);
                    BetDetailVO betDetailVO = JSON.parseObject(vvString, BetDetailVO.class);
                    betDetailVO.setGameCode(gameCode);
                    list.add(betDetailVO);
                });
            }
            return ResponseResult.SUCCESS(list);
        }
    }
}
