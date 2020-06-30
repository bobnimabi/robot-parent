package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.GameChild;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.utils.project.DateUtils;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.bbin.base.ao.JuQueryDetailAO;
import com.robot.bbin.base.ao.TotalBetGameAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.bo.JuQueryDetailBO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用：查询XBB消消除层数
 * @Author mrt
 * @Date 2020/6/2 14:56
 * @Version 2.0
 */
@Service
public class XBBBreakServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private OrderQueryServer orderQueryServer;

    @Autowired
    private XBBDetailServer xBBDetailServer;

    //查询用户id
    @Autowired
    private QueryBalanceFunction queryBalanceServer;



    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        OrderNoQueryDTO queryDTO = paramWrapper.getObj();


        // 通用局查询(注单查询)
        Response<JuQueryBO> response = orderQueryServer.doFunction(paramWrapper, robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }


        // 组装局查询细节参数并校验
        JuQueryBO juQueryBO = response.getObj();
       JuQueryDetailBO juQueryDetailBO = MyBeanUtil.copyProperties(juQueryBO, JuQueryDetailBO.class);
        Response<ParamWrapper<JuQueryDetailAO>> params = juQueryDetailAO(queryDTO, juQueryBO);
        if (!params.isSuccess()) {
            return params;
        }

        // 查询余额：查询UserID
        Response<QueryBalanceBO> balanceResult = queryBalanceServer.doFunction(createQueryBalanceParams(paramWrapper), robotWrapper);
        if (!balanceResult.isSuccess()) {
            return balanceResult;
        }



        // 局查询细节（xbb消消除层数）
        Response<ParamWrapper<JuQueryDetailAO>> paramWrapperResponse = juQueryDetailAO(queryDTO, juQueryBO);
        if (!paramWrapperResponse.isSuccess()) {
            return paramWrapperResponse;
        }
        Response<Integer> detailResponse = xBBDetailServer.doFunction(paramWrapperResponse.getObj(), robotWrapper);

        if (!detailResponse.isSuccess()) {
            return detailResponse;
        }
        juQueryDetailBO.setLevel(detailResponse.getObj());

        return Response.SUCCESS(juQueryDetailBO);

    }

    /**
     * 组装查询余额参数    /userID
     * @param paramWrapper
     * @return
     */
    private ParamWrapper<String> createQueryBalanceParams(ParamWrapper<OrderNoQueryDTO> paramWrapper) {
        OrderNoQueryDTO breakThroughDTO = paramWrapper.getObj();
        return new ParamWrapper<String>(breakThroughDTO.getUserName());
    }




    /**
     * 局查询细节参数组装
     * 顺便就检查游戏在不在Children的范围内
     */
    private Response<ParamWrapper<JuQueryDetailAO>> juQueryDetailAO(OrderNoQueryDTO queryDTO, JuQueryBO juQueryBO) {
        JuQueryDetailAO ao = new JuQueryDetailAO();
        // 设置注单号
        ao.setOrderNo(juQueryBO.getPlatFormOrderNo());
        // 设置平台编码
        ao.setPageId(queryDTO.getGameCode());
        // TODO 还有个userId未设置,现在逻辑上都搞定了,这个是唯一的问题了
        ao.setUserId(juQueryBO.getUserId());
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
