package com.robot.jiuwu.pay.function;

import com.bbin.common.response.ResponseResult;
import com.robot.center.execute.IPathEnum;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.pool.RobotWrapper;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.base.dto.DoLockDTO;
import com.robot.jiuwu.base.dto.UpdateRemark2DTO;
import com.robot.jiuwu.base.function.DoLockServer;
import com.robot.jiuwu.base.function.UpdateRemark2Server;
import com.robot.jiuwu.base.vo.WithdrawListRowsData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

/**
 * Created by mrt on 2020/1/10 0010 16:26
 */
public class LockAndRemarkServer extends FunctionBase<List<WithdrawListRowsData>> {
    @Autowired
    private UpdateRemark2Server updateRemark2Server;
    @Autowired
    private DoLockServer doLockServer;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<List<WithdrawListRowsData>> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        List<WithdrawListRowsData> rows = paramWrapper.getObj();
        Iterator<WithdrawListRowsData> iterator = rows.iterator();
        while (iterator.hasNext()) {
            WithdrawListRowsData  row = iterator.next();
            // 锁定
            ResponseResult lockResult = doLock(row, robotWrapper);
            if (lockResult.isSuccess()) {
                // 备注
                ResponseResult remarkResult = remark(row, robotWrapper);
                if (remarkResult.isSuccess()) {
                    continue;
                }
            }
            iterator.remove();
        }
        return ResponseResult.SUCCESS();
    }

    @Override
    public IPathEnum getActionEnum() {
        return null;
    }

    /**
     * 加入工单
     */
    private ResponseResult doLock(WithdrawListRowsData row, RobotWrapper robotWrapper) throws Exception {
        ResponseResult responseResult = doLockServer.doFunction(new ParamWrapper<DoLockDTO>(new DoLockDTO(row.getId())), robotWrapper);
        return responseResult;
    }

    /**
     * 备注
     */
    private ResponseResult remark(WithdrawListRowsData row, RobotWrapper robotWrapper) throws Exception {
        UpdateRemark2DTO updateRemark2DTO = new UpdateRemark2DTO(row.getId(), "自动出款中：" + robotWrapper.getPlatformAccount());
        return updateRemark2Server.doFunction(new ParamWrapper<UpdateRemark2DTO>(updateRemark2DTO), robotWrapper);
    }
}
