package com.robot.xiaoka.income.client;

import bom.bbin.common.dto.income.robot.CancelCardDTO;
import bom.bbin.common.dto.income.robot.WithDrawDTO;
import com.bbin.common.dto.income.IncomeBackQueryDTO;
import com.bbin.common.dto.income.IncomeUserQueryDTO;
import com.bbin.common.dto.income.IncomeUserUpdateDTO;
import com.bbin.common.response.ResponseResult;
import com.robot.center.function.ParamWrapper;
import com.robot.code.dto.LoginDTO;
import com.robot.suxiao.base.basic.FunctionEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "robot-xiaoka-income-server",path = "/xiaokaIncomeRobot")
public interface XiaoKaRobotClient {
    /**
     * 机器人：强制下线
     */
    @PostMapping("/closeRobot")
    public ResponseResult closeRobot(@RequestBody LoginDTO robotDTO) throws Exception;

    /**
     * 机器人：分页查询
     */
    @PostMapping("/pageRobot")
    public ResponseResult pageRobot(@RequestBody LoginDTO robotDTO) throws Exception;

    /**
     * 登录
     * @param robotDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/robotLogin")
    public ResponseResult robotLogin(LoginDTO robotDTO) throws Exception;

    /**
     * 销卡（单张）
     * @return
     * @throws Exception
     */
    @PostMapping("/cancelCard")
    public ResponseResult cancelCard(CancelCardDTO cardDTO) throws Exception;

    /**
     * 提现
     * @return
     * @throws Exception
     */
    @PostMapping("/withdraw")
    public ResponseResult withdraw(WithDrawDTO withDrawDTO) throws Exception;
}