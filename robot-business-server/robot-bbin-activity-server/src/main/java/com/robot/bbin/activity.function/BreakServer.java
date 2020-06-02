package com.robot.bbin.activity.function;

import com.bbin.common.dto.order.GameChild;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.bbin.activity.vo.BreakerQueryVO;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.ao.JuQueryDetailAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.function.JuQueryDetailServer;
import com.robot.bbin.base.function.JuQueryServer;
import com.robot.code.dto.Response;
import com.robot.core.function.base.IFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author mrt
 * @Date 2020/6/2 14:56
 * @Version 2.0
 * 消消除
 */
@Service
public class BreakServer implements IFunction<OrderNoQueryDTO,Object,BreakerQueryVO> {
    @Autowired
    private OrderQueryServer orderQueryServer;

    @Autowired
    private JuQueryDetailServer juQueryDetailServer;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        // 局查询
        Response<JuQueryBO> response = orderQueryServer.doFunction(paramWrapper, robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }

        // 组装局查询细节参数并校验
        JuQueryBO juQueryBO = response.getObj();
        Response<ParamWrapper<JuQueryDetailAO>> params = juQueryDetailAO(queryDTO, juQueryBO);
        if (!params.isSuccess()) {
            return params;
        }

        // 局查询细节（消消除层数）
        Response<Integer> detailResponse = juQueryDetailServer.doFunction(params.getObj(), robotWrapper);
        if (!detailResponse.isSuccess()) {
            return detailResponse;
        }

        BreakerQueryVO breakerQueryVO = MyBeanUtil.copyProperties(juQueryBO, BreakerQueryVO.class);
        breakerQueryVO.setAccumulativeWins(detailResponse.getObj());
        return Response.SUCCESS(breakerQueryVO);
    }

    /**
     * 局查询细节参数组装
      */
    private Response<ParamWrapper<JuQueryDetailAO>> juQueryDetailAO(OrderNoQueryDTO queryDTO, JuQueryBO juQueryBO) {
        JuQueryDetailAO ao = new JuQueryDetailAO();
        ao.setPageId(juQueryBO.getPageId());
        ao.setKey(juQueryBO.getKey());
        String gameName = juQueryBO.getGameName();
        boolean flag = false;
        for (GameChild child : queryDTO.getChildren()) {
            if (gameName.equals(child.getName())) {
                ao.setGameType(child.getGameCode());
                flag = true;
                break;
            }
        }
        if (!flag) {
            return Response.FAIL("消消除：参数的children里面未包含此游戏：" + gameName);
        }
        return Response.SUCCESS(new ParamWrapper<JuQueryDetailAO>(ao));
    }
}
