package com.robot.bbin.base.function;

import com.robot.bbin.base.basic.PathEnum;
import com.robot.bbin.base.dto.JuQueryDetailDTO;
import com.robot.code.dto.Response;
import com.robot.core.function.base.AbstractFunction;
import com.robot.core.function.base.IPathEnum;
import com.robot.core.function.base.IResultHandler;
import com.robot.core.http.request.ICustomEntity;
import com.robot.core.http.request.UrlEntity;
import com.robot.core.http.response.StanderHttpResponse;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 11/15/2019 12:29 PM
 * 局查询弹窗
 */
@Service
public class JuQueryDetailServer extends AbstractFunction<JuQueryDetailDTO,String,String> {

    @Override
    protected IPathEnum getPathEnum() {
        return PathEnum.JU_QUERY_DETAIL;
    }

    @Override
    protected ICustomEntity getEntity(JuQueryDetailDTO queryDTO) {
        return UrlEntity.custom(5)
                .add("lang", "cn")
                .add("wid", queryDTO.getOrderNo())// 注单编号
                .add("id", queryDTO.getPageId()) // 平台编码
                .add("gametype", queryDTO.getGameType()) // 游戏编码
                .add("key", queryDTO.getKey()); // 页面携带参数
    }

    @Override
    protected IResultHandler<String, String> getResultHandler() {
        return ResultHandler.INSTANCE;
    }

    /**
     * 响应结果转换类
     */
    private static final class ResultHandler implements IResultHandler<String,String> {
        private static final ResultHandler INSTANCE = new ResultHandler();
        private ResultHandler(){}

        @Override
        public Response parse2Obj(StanderHttpResponse<String,String> srp) {
            return Response.SUCCESS(srp.getOriginalEntity());
        }
    }
}
