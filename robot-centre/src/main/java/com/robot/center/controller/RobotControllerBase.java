package com.robot.center.controller;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.util.ThreadLocalUtils;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.center.constant.RobotConsts;
import com.robot.center.function.IFunction;
import com.robot.center.function.IFunctionEnum;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.IRobotKeepAlive;
import com.robot.center.pool.RobotManager;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.TContext;
import com.robot.code.dto.LoginDTO;
import com.robot.code.entity.TenantRobot;
import com.robot.code.entity.TenantRobotRecord;
import com.robot.code.service.ITenantRobotRecordService;
import com.robot.code.service.ITenantRobotTemplateService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

/**
 * Created by mrt on 2019/5/16 0016 下午 7:41
 */
@Slf4j
public class RobotControllerBase {
    private static final String EXTERNAL_NO = RobotConsts.ROBOT_PROJECT_PERFIX + "EXTERNAL_NO:";

    @Autowired
    private Map<String, IFunction> functionMap;
    @Autowired
    private RobotManager robotManager;
    @Autowired
    private IRobotKeepAlive robotKeepAlive;
    @Autowired
    private ITenantRobotTemplateService templateService;
    @Autowired+
    private ITenantRobotRecordService robotRecordService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("机器人：登录")
    @PostMapping("/robotLogin")
    public ResponseResult robotLogin(@RequestBody LoginDTO robotDTO) throws Exception {
        if (null == robotDTO || null == robotDTO.getId()) {
            return ResponseResult.FAIL("未传入参数");
        }
        return robotKeepAlive.login(robotDTO);
    }

    @ApiOperation("机器人：增加")
    @PostMapping("/addRobot")
    public ResponseResult addRobot(@RequestBody LoginDTO robotDTO) throws Exception {
        if (null == robotDTO
                || StringUtils.isEmpty(robotDTO.getPlatformAccount())
                || StringUtils.isEmpty(robotDTO.getPlatformPassword())) {
            return ResponseResult.FAIL("账号或密码为空");
        }
        return robotManager.addRobot(robotDTO);
    }

    @ApiOperation("机器人：删除")
    @PostMapping("/deleteRobot")
    public ResponseResult deleteRobot(@RequestBody LoginDTO robotDTO) throws Exception {
        if (null == robotDTO || null == robotDTO.getId()) {
            return ResponseResult.FAIL("请传入机器人ID");
        }
        return robotManager.deleteRobot(robotDTO.getId());
    }

    @ApiOperation("机器人：修改")
    @PostMapping("/updateRobot")
    public ResponseResult updateRobot(@RequestBody LoginDTO robotDTO) throws Exception {
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
    public ResponseResult pageRobot(@RequestBody LoginDTO robotDTO) throws Exception {
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
    public ResponseResult closeRobot(@RequestBody LoginDTO robotDTO) throws Exception {
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

    /**
     * 任务分发
     * @param paramWrapper 参数
     * @param functionEnum 功能枚举
     * @return
     * @throws Exception
     */
    protected ResponseResult distribute(ParamWrapper paramWrapper, IFunctionEnum functionEnum) throws Exception{
        IFunction iFunction = functionMap.get(functionEnum.getFunctionServer());
        Assert.notNull(iFunction, "获取Function失败，FunctionServer:" + functionEnum.getFunctionServer());
        ResponseResult responseResult = iFunction.doFunction(paramWrapper);
        if (responseResult.isSuccess()) {
            return ResponseResult.SUCCESS(JSON.toJSONString(responseResult.getObj()));
        }
        return responseResult;
    }

    protected ResponseResult distributeByRobot(ParamWrapper paramWrapper, IFunctionEnum functionEnum, Long robotId) throws Exception{
        ResponseResult responseResult = robotManager.getRobotById(robotId);
        if (!responseResult.isSuccess()) {
            return responseResult;
        }
        TenantRobot robot = (TenantRobot) responseResult.getObj();
        RobotWrapper robotWrapper = MyBeanUtil.copyProperties(robot, RobotWrapper.class);
        IFunction iFunction = functionMap.get(functionEnum.getFunctionServer());
        Assert.notNull(iFunction, "获取Function失败，FunctionServer:" + functionEnum.getFunctionServer());
        ResponseResult result = iFunction.doFunction(paramWrapper,robotWrapper);
        return result;
    }

    /**
     * 租户id等信息填充
     * @return
     */
    protected boolean tenantDispatcher(long platformId,long function) {
        try {
            TContext.setTenantId(ThreadLocalUtils.getTenantIdOption().get());
            TContext.setChannelId(ThreadLocalUtils.getChannelIdOption().get());
            TContext.setPlatformId(platformId);
            TContext.setFunction(function);
            return true;
        } catch (Exception e) {
            log.info("未获取到tenant相关,{},{},{},{}", TContext.getTenantId(), TContext.getChannelId(), TContext.getPlatformId(), TContext.getFunction());
        } finally {
            ThreadLocalUtils.clean();
        }
        return false;
    }

    /**
     * 外部订单号重复性检查
     * @param externalNo 外部订单号
     * @return
     */
    protected boolean isRedo(String externalNo) {
        // redis检查
        String cacheKey = createExteralNoCacheKey(externalNo);
        Boolean isSave = stringRedisTemplate.opsForValue().setIfAbsent(cacheKey, "", Duration.ofDays(10));
        if (!isSave) {
            log.info("redis:该外部订单已经存在：{}", externalNo);
            return true;
        }

        // mysql检查
        TenantRobotRecord robotRecord = robotRecordService.getRecordByExternalOrderNo(externalNo);
        if (null != robotRecord) {
            log.info("mysql:该外部订单已经存在：{}", externalNo);
            return true;
        }
        return false;
    }

    // 组装redis-key：外部订单号
    private String createExteralNoCacheKey(String externalNo) {
        Long tenantId = TContext.getTenantId();
        Long channelId = TContext.getChannelId();
        Long platformId = RobotConsts.PLATFORM_ID.BBIN;
        Long functionCode = RobotConsts.FUNCTION_CODE.ACTIVITY;
        return new StringBuilder(30)
                .append(EXTERNAL_NO)
                .append(tenantId).append(":")
                .append(channelId).append(":")
                .append(platformId).append(":")
                .append(functionCode).append(":")
                .append(externalNo)
                .toString();
    }
}
