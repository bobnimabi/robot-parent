package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.ao.TotalBetGameAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.bo.TotalBetGameBO;
import com.robot.bbin.base.function.JuQueryFunction;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.bbin.base.function.TotalBetGameFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询游戏总投注
 * @Author mrt
 * @Date 2020/6/3 12:05
 * @Version 2.0
 */
@Service
public class GameBetServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private JuQueryFunction juQueryFunction;
    @Autowired
    private TotalBetGameFunction totalBetGameFunction;
    @Autowired
    private QueryBalanceFunction queryBalanceServer;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO queryDTO = (OrderNoQueryDTO)paramWrapper.getObj();
        // 查询余额：查询UserID
        Response<QueryBalanceBO> balanceResult = queryBalanceServer.doFunction(createQueryBalanceParams(paramWrapper), robotWrapper);
        if (!balanceResult.isSuccess()) {
            return balanceResult;
        }
        QueryBalanceBO balanceBO = balanceResult.getObj();

        Response<JuQueryBO> juResponse = this.juQueryFunction.doFunction(juQueryAO(queryDTO), robotWrapper);


        // 查询游戏总投注
        Response<List<TotalBetGameBO>> betResult = totalBetGameFunction.doFunction(createBetParams(paramWrapper, balanceBO), robotWrapper);
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



    private ParamWrapper<JuQueryAO> juQueryAO(OrderNoQueryDTO queryDTO) {
        JuQueryAO juQueryAO = new JuQueryAO();
        juQueryAO.setGameKind(queryDTO.getGameCode());
        juQueryAO.setOrderNo(queryDTO.getOrderNo());
        juQueryAO.setBarId("2");
        return new ParamWrapper(juQueryAO);
    }

    /**
     * 组装投注查询参数
     * @param paramWrapper
     * @param balanceBO
     * @return
     */
    private ParamWrapper<TotalBetGameAO> createBetParams(ParamWrapper<OrderNoQueryDTO> paramWrapper,QueryBalanceBO balanceBO) {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        TotalBetGameAO gameDTO = new TotalBetGameAO();
        gameDTO.setDateStart(queryDTO.getStartDate().format(DateUtils.DF_3));

        gameDTO.setDateEnd(queryDTO.getEndDate().format(DateUtils.DF_3));
        gameDTO.setUserID(balanceBO.getUser_id());
        gameDTO.setGameKind(queryDTO.getGameCode());
        return new ParamWrapper<TotalBetGameAO>(gameDTO);
    }
}
