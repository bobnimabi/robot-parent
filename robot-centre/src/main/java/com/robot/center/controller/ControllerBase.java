package com.robot.center.controller;

import com.bbin.common.util.ThreadLocalUtils;
import com.robot.code.dto.LoginDTO;
import com.robot.code.dto.Response;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.service.ITenantRobotTemplateService;
import com.robot.core.common.TContext;
import com.robot.core.robot.manager.IControllerFacde;
import com.robot.core.task.dispatcher.IDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mrt on 2019/5/16 0016 下午 7:41
 */
@Slf4j
public class ControllerBase {

    @Autowired
    private IControllerFacde controllerFacde;

    @Autowired
    private ITenantRobotTemplateService templateService;

    @Autowired
    protected IDispatcher dispatcher;

    /**
     * 机器人：登录
     * 路径统一：/robotLogin
     */

    /**
     * 机器人：增加
     * @return
     */
    @PostMapping("/addRobot")
    public Response addRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO
                || StringUtils.isEmpty(robotDTO.getPlatformAccount())
                || StringUtils.isEmpty(robotDTO.getPlatformPassword())) {
            return Response.FAIL("账号或密码为空");
        }
        return controllerFacde.addRobot(robotDTO);
    }

    /**
     * 机器人：删除
     */
    @PostMapping("/deleteRobot")
    public Response deleteRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO || null == robotDTO.getId()) {
            return Response.FAIL("请传入机器人ID");
        }
        return controllerFacde.deleteRobot(robotDTO.getId());
    }

    /**
     * 机器人：修改
     */
    @PostMapping("/updateRobot")
    public Response updateRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        if (null == robotDTO
                || null == robotDTO.getId()
                || StringUtils.isEmpty(robotDTO.getPlatformAccount())
                || StringUtils.isEmpty(robotDTO.getPlatformPassword())) {
            return Response.FAIL("账号或密码为空");
        }
        return controllerFacde.updateRobot(robotDTO);
    }

    /**
     * 机器人：分页查询
     */
    @PostMapping("/pageRobot")
    public Response pageRobot(@RequestBody TenantRobotDTO robotDTO) throws Exception {
        return controllerFacde.pageRobot(robotDTO);
    }

    /**
     * 机器人：根据id查询
     */
    @GetMapping("/getRobotById")
    public Response getRobotById(@RequestParam Long id) throws Exception {
        if (null == id) {
            return Response.FAIL("请传入机器人ID");
        }
        return controllerFacde.getRobotById(id);
    }

    /**
     * 机器人：强制下线
     */
    @PostMapping("/closeRobot")
    public Response closeRobot(@RequestBody LoginDTO robotDTO) throws Exception {
        if (null == robotDTO || null == robotDTO.getId()) {
            return Response.FAIL("请传入机器人ID");
        }
        boolean offline = controllerFacde.offline(robotDTO.getId());
        if (offline) {
            return Response.SUCCESS();
        }
        return Response.FAIL("强制下线失败");
    }

    /**
     * 机器人：获取模板
     */
    @GetMapping("/getTemplate")
    public Response getTemplate() throws Exception {
        return Response.SUCCESS(templateService.getTemplate());
    }

    /**
     * 租户id等信息填充
     * @return
     */
    protected void handleTenant(String platformId, String function) {
        try {
            TContext.setTenantId(ThreadLocalUtils.getTenantIdOption().get() + "");
            TContext.setChannelId(ThreadLocalUtils.getChannelIdOption().get() + "");
            TContext.setPlatformId(platformId);
            TContext.setFunction(function);
        } catch (Exception e) {
            throw new IllegalArgumentException("MQ：未获取到相关租户信息", e);
        } finally {
            ThreadLocalUtils.clean();
        }
    }
}
