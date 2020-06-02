package com.robot.bbin.base.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.dto.BetDetailDTO;
import com.robot.bbin.base.vo.BetDetailVO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 下注查询
 */
@Service
public class BetDetailServer extends AbstractFunction<BetDetailDTO,String,List<BetDetailVO>> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.BET_ANALYSIS_DETAIL;
    }

    @Override
    protected ICustomEntity getEntity(BetDetailDTO betDTO, RobotWrapper robotWrapper) {
        return UrlEntity.custom(6)
                .add("listid", betDTO.getListid())
                .add("start", betDTO.getStart())
                .add("end", betDTO.getEnd())
                .add("game", betDTO.getGame())
                .add("currency", betDTO.getCurrency())
                .add("gametype", betDTO.getGametype());
    }

    @Override
    protected IResultHandler<String, List<BetDetailVO>> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果处理
     */
    private static final class ResultHandler implements IResultHandler<String, List<BetDetailVO>> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String, List<BetDetailVO>> shr) {
            List<BetDetailVO> list = new ArrayList<>(1);
            JSONObject jsonObject = JSON.parseObject(shr.getOriginalEntity());
            Collection<Object> values = jsonObject.values();
            for (Object value : values) {
                Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) value;
                map.forEach((gameCode,v)->{
                    Map<String, String> vv = (Map<String, String>) v;
                    String vvString = JSON.toJSONString(vv);
                    BetDetailVO betDetailVO = JSON.parseObject(vvString, BetDetailVO.class);
                    betDetailVO.setGameCode(gameCode.substring(4));
                    list.add(betDetailVO);
                });
            }
            return Response.SUCCESS(list);
        }
    }
}
