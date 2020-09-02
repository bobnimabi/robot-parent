package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.robot.bbin.base.ao.TotalBetGameAO;
import com.robot.bbin.base.ao.XBBGetGameCodeAO;
import com.robot.bbin.base.ao.XBBTotalBetGameAO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.bo.TotalBetGameBO;
import com.robot.bbin.base.bo.XBBTotalBetGameBO;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.bbin.base.function.TotalBetGameFunction;
import com.robot.bbin.base.function.XBBGetGameCodeFunction;
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
    private XBBGetGameCodeFunction getGameCodeFunction;

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

        //根据注单号查询游戏名称 获取gamecode
        Response<String> gameNameResult = getGameCodeFunction.doFunction(createGameCodeParams(paramWrapper, balanceBO), robotWrapper);
        String gameName = gameNameResult.getObj();

        // 查询游戏总投注
        Response<XBBTotalBetGameBO> betResult = totalBetGame.doFunction(createBetParams(paramWrapper, balanceBO,gameName), robotWrapper);
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
     * 组装查询GameCode参数
     * @param paramWrapper
     * @return
     */
    private ParamWrapper<XBBGetGameCodeAO> createGameCodeParams(ParamWrapper<OrderNoQueryDTO> paramWrapper,QueryBalanceBO balanceBO) {

        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        XBBGetGameCodeAO gameCodeAO = new XBBGetGameCodeAO();
          gameCodeAO.setGameKind(queryDTO.getGameCode());
          gameCodeAO.setOrderNo(queryDTO.getOrderNo());
          gameCodeAO.setUserID(balanceBO.getUser_id());
        return new ParamWrapper<XBBGetGameCodeAO>(gameCodeAO);
    }


    /**
     * 组装投注查询参数
     * @param paramWrapper
     * @param balanceBO
     * @return
     */
    private ParamWrapper<XBBTotalBetGameAO> createBetParams(ParamWrapper<OrderNoQueryDTO> paramWrapper,QueryBalanceBO balanceBO,String gameName) {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();

        XBBTotalBetGameAO gameDTO = new XBBTotalBetGameAO();
        gameDTO.setDateStart(queryDTO.getStartDate().format(DateUtils.DF_3));
        gameDTO.setDateEnd(queryDTO.getEndDate().format(DateUtils.DF_3));
        gameDTO.setUserID(balanceBO.getUser_id());
        gameDTO.setGameKind(queryDTO.getGameCode());
        gameDTO.setBarId("1");
        switch (gameName) {

            case "糖果派对":

                gameDTO.setGameType(queryDTO.getChildren().get(0).getGameCode());

                break;

            case "连环夺宝":

                gameDTO.setGameType(queryDTO.getChildren().get(1).getGameCode());

                break;

            case "连环夺宝2":

                gameDTO.setGameType(queryDTO.getChildren().get(2).getGameCode());

                break;
            case "糖果派对2":

                gameDTO.setGameType(queryDTO.getChildren().get(3).getGameCode());

                break;
            case "糖果王国":

                gameDTO.setGameType(queryDTO.getChildren().get(4).getGameCode());

                break;
            default:
                gameDTO.setGameType("0");
                break;
        }

        return new ParamWrapper<XBBTotalBetGameAO>(gameDTO);
    }

}
