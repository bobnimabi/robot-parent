package com.robot.code.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbin.utils.project.MyBeanUtil;
import com.robot.code.response.Response;
import com.robot.code.dto.TenantRobotDTO;
import com.robot.code.entity.Platform;
import com.robot.code.entity.TenantRobot;
import com.robot.code.mapper.TenantRobotMapper;
import com.robot.code.service.IPlatformService;
import com.robot.code.service.ITenantRobotService;
import com.robot.code.vo.TenantRobotVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机器人表 服务实现类
 * </p>
 * @author admin
 * @since 2019-10-21
 */
@Slf4j
@Service
public class TenantRobotServiceImpl extends ServiceImpl<TenantRobotMapper, TenantRobot> implements ITenantRobotService {

    @Autowired
    private IPlatformService platformService;

    @Override
    public Response addRobot(TenantRobotDTO robotDTO) {
        //是否重复添加
        if (isRobotRepetByAdd(robotDTO.getPlatformAccount())) {
            return Response.FAIL("账号已存在");
        }
        TenantRobot robot = MyBeanUtil.copyProperties(robotDTO, TenantRobot.class);
        return save(robot) ? Response.SUCCESS() : Response.FAIL("添加失败");
    }

    @Override
    public Response deleteRobot(long robotId) {
        // 删除
        boolean isRemove = removeById(robotId);
        if (!isRemove) {
            throw new IllegalStateException("表:删除机器人失败");
        }
        return Response.SUCCESS();
    }

    @Override
    public Response updateRobot(TenantRobotDTO robotDTO) {
        // 是否重复添加
        if (isRobotRepetByUpdate(robotDTO.getId(),robotDTO.getPlatformAccount())) {
            return Response.FAIL("账号重复");
        }
        // 修改
        TenantRobot robot = MyBeanUtil.copyProperties(robotDTO, TenantRobot.class);
        boolean isUpdate = updateById(robot);
        if (!isUpdate) {
            throw new IllegalStateException("表:修改机器人失败");
        }
        return Response.SUCCESS();
    }

    @Override
    public Response pageRobot(TenantRobotDTO robotDTO){
        IPage page = page(robotDTO, new LambdaQueryWrapper<TenantRobot>().orderByDesc(TenantRobot::getGmtCreateTime));
        Page<TenantRobotVO> voPage = MyBeanUtil.copyPageToPage(page, TenantRobotVO.class);
        for (TenantRobotVO robotVO : voPage.getRecords()) {
            Platform platform = platformService.getById(robotVO.getPlatformId());
            if (null != platform) {
                robotVO.setPlatformName(platform.getPlatformName());
            }
        }
        return Response.SUCCESS(voPage);
    }

    @Override
    public Response getRobotById(long robotId) {
        TenantRobot robot = getById(robotId);
        if (null == robot) {
            return Response.FAIL("检查id是否正确");
        }
        return Response.SUCCESS(MyBeanUtil.copyProperties(robot, TenantRobotVO.class));
    }

    @Override
    public boolean offlineDB(long robotId) {
        // 数据库：下线机器人
        return update(new LambdaUpdateWrapper<TenantRobot>()
                .eq(TenantRobot::getId, robotId)
                .set(TenantRobot::getIsOnline, false)
                .set(TenantRobot::getInfo, "已离线..."));
    }

    @Override
    public boolean onlineDB(long robotId) {
        // 数据库：上线机器人
        return update(new LambdaUpdateWrapper<TenantRobot>()
                .eq(TenantRobot::getId, robotId)
                .set(TenantRobot::getIsOnline, true)
                .set(TenantRobot::getInfo, "运行中..."));
    }

    // 新增机器人：查看机器人是否重复添加
    private boolean isRobotRepetByAdd(String platformAccount) {
        return null != getOne(new LambdaQueryWrapper<TenantRobot>().eq(TenantRobot::getPlatformAccount, platformAccount));
    }

    // 更新机器人：查看机器人是否重复添加
    private boolean isRobotRepetByUpdate(long robotId,String platformAccount){
        TenantRobot robot = getOne(new LambdaQueryWrapper<TenantRobot>().eq(TenantRobot::getPlatformAccount, platformAccount));
        return null != robot && robotId != robot.getId();
    }
}
