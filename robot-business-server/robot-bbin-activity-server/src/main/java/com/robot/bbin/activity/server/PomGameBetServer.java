package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.ao.TotalBetGameAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.bo.TotalBetGameBO;
import com.robot.bbin.base.function.JuQueryFunction;
import com.robot.bbin.base.function.PomTotalBetGameFunction;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Tanke
 * @date  2020/10/17
 * @desc :彩球加赠查询总下注
 */
@Service
public class PomGameBetServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private PomTotalBetGameFunction pomTotalBetGameFunction;

    @Autowired
    private QueryBalanceFunction queryBalanceServer;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {

        // 查询余额：查询UserID
        Response<QueryBalanceBO> balanceResult = queryBalanceServer.doFunction(createQueryBalanceParams(paramWrapper), robotWrapper);
        if (!balanceResult.isSuccess()) {
            return balanceResult;
        }
        QueryBalanceBO balanceBO = balanceResult.getObj();



        // 查询游戏总投注
        Response<List<TotalBetGameBO>> betResult = pomTotalBetGameFunction.doFunction(createBetParams(paramWrapper, balanceBO), robotWrapper);
        return betResult;
    }

    /**
     * 组装查询余额参数
     * @param paramWrapper
     * @return
     */
    private ParamWrapper<String> createQueryBalanceParams(ParamWrapper<OrderNoQueryDTO> paramWrapper) {
        OrderNoQueryDTO breakThroughDTO = paramWrapper.getObj();
        return new ParamWrapper<String>(breakThroughDTO.getUserName());
    }

    /**
     * 组装投注查询参数
     * @param paramWrapper
     * @param balanceBO
     * @return
     */
    private ParamWrapper<TotalBetGameAO> createBetParams(ParamWrapper<OrderNoQueryDTO> paramWrapper, QueryBalanceBO balanceBO ) {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        TotalBetGameAO gameDTO = new TotalBetGameAO();
        gameDTO.setDateEnd(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toString());
        gameDTO.setUserID(balanceBO.getUser_id());
        gameDTO.setGameKind(queryDTO.getGameCode());
        return new ParamWrapper<TotalBetGameAO>(gameDTO);
    }
}

 