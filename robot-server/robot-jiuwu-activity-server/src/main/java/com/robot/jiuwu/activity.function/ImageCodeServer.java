package com.robot.jiuwu.activity.function;

import com.alibaba.fastjson.JSON;
import com.bbin.common.response.ResponseResult;
import com.robot.center.constant.RobotConsts;
import com.robot.center.execute.IActionEnum;
import com.robot.center.execute.IResultParse;
import com.robot.center.function.FunctionBase;
import com.robot.center.function.ParamWrapper;
import com.robot.center.httpclient.CustomHttpMethod;
import com.robot.center.httpclient.StanderHttpResponse;
import com.robot.center.pool.RobotWrapper;
import com.robot.center.tenant.RobotThreadLocalUtils;
import com.robot.code.entity.TenantRobotAction;
import com.robot.jiuwu.activity.basic.ActionEnum;
import com.robot.jiuwu.activity.common.Constant;
import com.robot.jiuwu.activity.vo.ImageCodeResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 图片验证码
 */
@Slf4j
@Service
public class ImageCodeServer extends FunctionBase<Object> {

    @Autowired
    private StringRedisTemplate redis;

    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<Object> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, null, null, ImageCodeParse.INSTANCE);
        ResponseResult imageResponse = standerHttpResponse.getResponseResult();
        if (!imageResponse.isSuccess()) {
            return imageResponse;
        }
        ImageCodeResultVO entity = (ImageCodeResultVO) imageResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        redis.opsForValue().set(createCacheKeyCaptchaToken(robotWrapper.getId()), entity.getData().getCaptchaToken(), Duration.ofMinutes(5));
        return ResponseResult.SUCCESS(entity.getData().getCaptchaToken());
    }

    @Override
    public IActionEnum getActionEnum() {
        return ActionEnum.IMAGE_CODE;
    }

    // 响应结果转换
    private static final class ImageCodeParse implements IResultParse{
        private static final ImageCodeParse INSTANCE = new ImageCodeParse();
        private ImageCodeParse(){}

        @Override
        public ResponseResult parse(String result) {
            if (StringUtils.isEmpty(result)) {
                return ResponseResult.FAIL("未响应");
            }
            ImageCodeResultVO imageCodeResult = JSON.parseObject(result, ImageCodeResultVO.class);
            if (null == imageCodeResult.getCode()) {
                return ResponseResult.FAIL("转换失败");
            }
            return ResponseResult.SUCCESS(imageCodeResult);
        }
    }

    // 创建图片验证码的缓存标志
    static String createCacheKeyCaptchaToken(long robotId) {
        return new StringBuilder(50)
                .append(RobotConsts.CAPTCHA_TOKEN)
                .append(RobotThreadLocalUtils.getTenantId()).append(":")
                .append(RobotThreadLocalUtils.getChannelId()).append(":")
                .append(RobotThreadLocalUtils.getPlatformId()).append(":")
                .append(RobotThreadLocalUtils.getFunction()).append(":")
                .append(robotId)
                .toString();
    }
}
