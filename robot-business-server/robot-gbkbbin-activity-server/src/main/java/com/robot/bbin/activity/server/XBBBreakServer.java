package com.robot.bbin.activity.server;
import com.bbin.common.dto.order.GameChild;
import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.bbin.common.util.DateUtils;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.bbin.base.ao.JuQueryDetailAO;
import com.robot.bbin.base.ao.XBBJuQueryDetailAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.bo.JuQueryDetailBO;
import com.robot.bbin.base.bo.XBBJuQueryDetailBO;
import com.robot.bbin.base.function.XBBJuQueryDetailFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 查询XBB消除层数
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 *
 */
@Service
public class XBBBreakServer implements IAssemFunction<OrderNoQueryDTO> {

    @Autowired
    private GetTokenServer tokenServer;

    //局查询获取游戏名字

    @Autowired
    private  XBBOrderQueryServer orderQueryServer;

    @Autowired
    private XBBJuQueryDetailFunction juQueryDetailFunction;

    //查询层数

    @Override
    public Response doFunction(ParamWrapper<OrderNoQueryDTO> paramWrapper, RobotWrapper robotWrapper) throws Exception {


        // 局查询
        Response<JuQueryBO> response = orderQueryServer.doFunction(paramWrapper, robotWrapper);
        if (!response.isSuccess()) {
            return response;
        }

        // 组装局查询细节参数并校验
        JuQueryBO juQueryBO = response.getObj();
        XBBJuQueryDetailBO xbbJuQueryDetailBO = MyBeanUtil.copyProperties(juQueryBO, XBBJuQueryDetailBO.class);


        // 局查询细节前置查询:获取Token
        Response<String> tokenResult = tokenServer.doFunction(paramWrapper, robotWrapper);
        if (!tokenResult.isSuccess()) {
            return tokenResult;
        }

        //组装查询层数并校验
        String token = tokenResult.getObj();


        Response<ParamWrapper<XBBJuQueryDetailAO>> params1 = xbbDetailParams(token);
        if (!params1.isSuccess()) {
            return params1;
        }



        // 查询消除次数(局查询细节)
        Response<Integer> detailResponse = juQueryDetailFunction.doFunction(params1.getObj(), robotWrapper);
        if (!detailResponse.isSuccess()) {
            return detailResponse;
        }

        xbbJuQueryDetailBO.setLevel(detailResponse.getObj());
        return Response.SUCCESS(xbbJuQueryDetailBO);


    }



/**
     *
     * 查询 局查询细节参数组装
     */

    private Response<ParamWrapper<XBBJuQueryDetailAO> > xbbDetailParams(String token ) {
        XBBJuQueryDetailAO juQueryDetailao = new XBBJuQueryDetailAO();
        juQueryDetailao.setToken(token);

        return Response.SUCCESS(new ParamWrapper<XBBJuQueryDetailAO> (juQueryDetailao));
    }



/**
     * 局查询细节参数组装
     */
    private Response<ParamWrapper<JuQueryDetailAO>> getjuQueryDetailAO(OrderNoQueryDTO queryDTO, JuQueryBO juQueryBO, JuQueryDetailBO juQueryDetailBO) {

        JuQueryDetailAO ao = new JuQueryDetailAO();
        ao.setPageId(juQueryBO.getPageId());
        ao.setKey(juQueryBO.getKey());
        ao.setOrderNo(queryDTO.getOrderNo());
        ao.setRounddate(juQueryBO.getOrderTime().format(DateUtils.DF_3));
        String gameName = juQueryBO.getGameName();
        boolean flag = false;
        for (GameChild child : queryDTO.getChildren()) {
            if (gameName.equals(child.getName())) {
                ao.setGameType(child.getGameCode());
                juQueryDetailBO.setGameCode(child.getGameCode());
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
