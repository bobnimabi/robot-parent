package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.TotalBetGameAO;
import com.robot.bbin.base.ao.XBBTotalBetGameAO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.bo.TotalBetGameBO;
import com.robot.bbin.base.bo.XBBTotalBetGameBO;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.bbin.base.function.TotalBetGameFunction;
import com.robot.bbin.base.function.XBBTotalBetGameFunction;
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
public class XBBGameBetServer implements IAssemFunction<OrderNoQueryDTO> {
    @Autowired
    private QueryBalanceFunction queryBalanceServer;

    @Autowired
    private XBBTotalBetGameFunction totalBetGame;

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        // 查询余额：查询UserID
        Response<QueryBalanceBO> balanceResult = queryBalanceServer.doFunction(createQueryBalanceParams(paramWrapper), robotWrapper);
        if (!balanceResult.isSuccess()) {
            return balanceResult;
        }
        QueryBalanceBO balanceBO = balanceResult.getObj();

        // 查询游戏总投注
        Response<List<XBBTotalBetGameBO>> betResult = totalBetGame.doFunction(createBetParams(paramWrapper, balanceBO), robotWrapper);
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
    private ParamWrapper<XBBTotalBetGameAO> createBetParams(ParamWrapper<OrderNoQueryDTO> paramWrapper,QueryBalanceBO balanceBO) {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        XBBTotalBetGameAO gameDTO = new XBBTotalBetGameAO();
        gameDTO.setDateStart(queryDTO.getStartDate().format(DateUtils.DF_3));
        gameDTO.setDateEnd(queryDTO.getEndDate().format(DateUtils.DF_3));
        gameDTO.setUserID(balanceBO.getUser_id());
        gameDTO.setGameKind(queryDTO.getGameCode());
        gameDTO.setGameType(queryDTO.getChildren().get(0).getGameCode());
        gameDTO.setBarId("1");
        return new ParamWrapper<XBBTotalBetGameAO>(gameDTO);
    }

    /*   SearchData: MemberBets
        BarID: 1
        GameKind: 76
        date_start: 2020-08-07
        date_end: 2020-08-07
        GameType: 76073
        Limit: 50
        Sort: DESC
        UserID: 617241636*/
}
