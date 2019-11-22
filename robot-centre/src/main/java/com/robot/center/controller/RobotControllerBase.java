package com.robot.center.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.util.ThreadLocalUtils;
import com.robot.center.constant.RobotConsts;
import com.robot.center.function.IFunction;
import com.robot.center.function.IFunctionEnum;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.IRobotKeepAlive;
import com.robot.center.pool.RobotManager;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.service.ITenantRobotTemplateService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by mrt on 2019/5/16 0016 下午 7:41
 */
@Slf4j
public class RobotControllerBase {
    @Autowired
    private Map<String, IFunction> functionMap;
    @Autowired
    private RobotManager robotManager;
    @Autowired
    private IRobotKeepAlive robotKeepAlive;
    @Autowired
    private ITenantRobotTemplateService templateService;

    @ApiOperation("机器人：登录")
    @PostMapping("/robotLogin")
    public ResponseResult robotLogin(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO || null == robotDTO.getId()) {
            return ResponseResult.FAIL("未传入参数");
        }
        return robotKeepAlive.login(robotDTO.getId(),robotDTO);
    }

    @ApiOperation("机器人：增加")
    @PostMapping("/addRobot")
    public ResponseResult addRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO
                || StringUtils.isEmpty(robotDTO.getPlatformAccount())
                || StringUtils.isEmpty(robotDTO.getPlatformPassword())) {
            return ResponseResult.FAIL("账号或密码为空");
        }
        return robotManager.addRobot(robotDTO);
    }

    @ApiOperation("机器人：删除")
    @PostMapping("/deleteRobot")
    public ResponseResult deleteRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO || null == robotDTO.getId()) {
            return ResponseResult.FAIL("请传入机器人ID");
        }
        return robotManager.deleteRobot(robotDTO.getId());
    }

    @ApiOperation("机器人：修改")
    @PostMapping("/updateRobot")
    public ResponseResult updateRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO
                || null == robotDTO.getId()
                || StringUtils.isEmpty(robotDTO.getPlatformAccount())
                || StringUtils.isEmpty(robotDTO.getPlatformPassword())) {
            return ResponseResult.FAIL("账号或密码为空");
        }
        return robotManager.updateRobot(robotDTO);
    }

    @ApiOperation("机器人：分页查询")
    @PostMapping("/pageRobot")
    public ResponseResult pageRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        return robotManager.pageRobot(robotDTO);
    }

    @ApiOperation("机器人：根据id查询")
    @GetMapping("/getRobotById")
    public ResponseResult getRobotById(@RequestParam Long id) throws Exception {
        if (null == id) {
            return ResponseResult.FAIL("请传入机器人ID");
        }
        return robotManager.getRobotById(id);
    }

    @ApiOperation("机器人：强制下线")
    @PostMapping("/closeRobot")
    public ResponseResult closeRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO || null == robotDTO.getId()) {
            return ResponseResult.FAIL("请传入机器人ID");
        }
        return robotManager.closeRobot(robotDTO.getId());
    }

    @ApiOperation("机器人：获取模板")
    @GetMapping("/getTemplate")
    public ResponseResult getTemplate() throws Exception {
        return ResponseResult.SUCCESS(templateService.getTemplate());
    }

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    protected ResponseResult distribute(ParamWrapper paramWrapper, IFunctionEnum functionEnum) throws Exception{
        IFunction iFunction = functionMap.get(functionEnum.getFunctionServer());
        Assert.notNull(iFunction, "获取Function失败，FunctionServer:" + functionEnum.getFunctionServer());
        ResponseResult responseResult = iFunction.doFunction(paramWrapper);
        if (responseResult.isSuccess()) {
            return ResponseResult.SUCCESS(JSON.toJSONString(responseResult.getObj()));
        }
        return responseResult;
    }

    protected void tenantDispatcher() {
        try {
            RobotThreadLocalUtils.setTenantId(ThreadLocalUtils.getTenantIdOption().get());
            RobotThreadLocalUtils.setChannelId(ThreadLocalUtils.getChannelIdOption().get());
            RobotThreadLocalUtils.setPlatformId(RobotConsts.PLATFORM_ID.BBIN);
            RobotThreadLocalUtils.setFunction(RobotConsts.FUNCTION_CODE.ACTIVITY);
        } finally {
            ThreadLocalUtils.clean();
        }
    }
}
