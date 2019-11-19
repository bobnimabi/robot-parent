package com.robot.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.robot.code.entity.TenantRobotDict;
import com.robot.code.mapper.TenantRobotDictMapper;
import com.robot.code.service.ITenantRobotDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 站点配置项 服务实现类
 * </p>
 *
 * @author admin
 * @since 2019-11-15
 */
@Service
public class TenantRobotDictServiceImpl extends ServiceImpl<TenantRobotDictMapper, TenantRobotDict> implements ITenantRobotDictService {

    public String getValue(String key) {
        TenantRobotDict dict = getOne(new LambdaQueryWrapper<TenantRobotDict>().eq(TenantRobotDict::getDictKey, key));
        if (null == dict) {
            throw new IllegalStateException("获取字典失败，dictKey：" + key);
        }
        return dict.getDictValue();
    }

}
