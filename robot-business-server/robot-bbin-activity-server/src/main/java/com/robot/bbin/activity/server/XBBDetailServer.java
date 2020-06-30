package com.robot.bbin.activity.server;

import com.bbin.common.dto.order.OrderNoQueryDTO;
import com.robot.bbin.base.ao.JuQueryAO;
import com.robot.bbin.base.ao.JuQueryDetailAO;
import com.robot.bbin.base.ao.XBBJuQueryAO;
import com.robot.bbin.base.bo.JuQueryBO;
import com.robot.bbin.base.bo.QueryBalanceBO;
import com.robot.bbin.base.function.QueryBalanceFunction;
import com.robot.bbin.base.function.XBBGetTokenFunction;
import com.robot.bbin.base.function.XBBJuQueryDetailFunction;
import com.robot.code.response.Response;
import com.robot.core.function.base.IAssemFunction;
import com.robot.core.function.base.ParamWrapper;
import com.robot.core.robot.manager.RobotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 局查询（所有平台均可使用）
 * 使用：查询注单
 * 注意：局查询页面必须是：
 *      请选择：注单编码
 * @Author mrt
 * @Date 2020/6/2 16:34
 * @Version 2.0
 *
 */
@Service
public class XBBDetailServer implements IAssemFunction<JuQueryDetailAO> {

    @Autowired
    private XBBGetTokenFunction tokenFunction;

    @Autowired
    private XBBJuQueryDetailFunction juQueryDetailServer;



    @Override
    public Response doFunction(ParamWrapper<JuQueryDetailAO> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        JuQueryDetailAO queryDTO = paramWrapper.getObj();
        // 局查询细节前置查询:获取Token
        Response<ArrayList<String>> tokenResult = tokenFunction.doFunction(tokenParams(queryDTO), robotWrapper);
        if (!tokenResult.isSuccess()) {
            return tokenResult;
        }
        ArrayList<String> tokens = tokenResult.getObj();

        // 查询消除次数(局查询细节)
        Response<Integer> response = juQueryDetailServer.doFunction(juQueryDetailAO(tokens.get(0)), robotWrapper);
        return response;





    }



    /**
     *  查询token所用参数组装
     */
    private ParamWrapper<XBBJuQueryAO> tokenParams(JuQueryDetailAO queryDTO) {
        XBBJuQueryAO tokendAO = new XBBJuQueryAO();
        // 平台编码
        tokendAO.setGamekind(queryDTO.getPageId());
        // userId,或许可以通过查询余额那个接口获取(待验证)
        tokendAO.setUserid(queryDTO.getUserId());
        // 注单号
        tokendAO.setWagersid(queryDTO.getOrderNo());
        return new ParamWrapper<>(tokendAO);
    }



    /**
     * 这个写法正确
     * 查询 局查询细节参数组装
     */
    // TODO 这里的参数是来自于TOken接口的调用的结果
    private ParamWrapper<JuQueryDetailAO> juQueryDetailAO(String token ) {
        JuQueryDetailAO juQueryDetailAO = new JuQueryDetailAO();
        juQueryDetailAO.setToken(token);

        return new ParamWrapper<JuQueryDetailAO>(juQueryDetailAO);
    }
}
