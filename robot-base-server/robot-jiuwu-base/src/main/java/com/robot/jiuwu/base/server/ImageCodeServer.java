package com.robot.jiuwu.base.server;


import com.robot.code.response.Response;
import com.robot.core.function.base.*;
import com.robot.core.robot.manager.RobotWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 图片验证码
 */
@Slf4j
@Service
public class ImageCodeServer implements IAssemFunction<Object> {

    @Override
    public Response doFunction(ParamWrapper<Object> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        return null;
    }



   /*


    @Override
    protected ResponseResult doFunctionFinal(ParamWrapper<Object> paramWrapper, RobotWrapper robotWrapper, TenantRobotAction action) throws Exception {
        // 执行
        StanderHttpResponse standerHttpResponse = execute.request(robotWrapper, CustomHttpMethod.POST_JSON, action, null, null, null, ImageCodeParse.INSTANCE, false);
        ResponseResult imageResponse = standerHttpResponse.getResponseResult();
        if (!imageResponse.isSuccess()) {
            return imageResponse;
        }
        ImageCodeResultVO entity = (ImageCodeResultVO) imageResponse.getObj();
        if (!Constant.SUCCESS.equals(entity.getCode())) {
            return ResponseResult.FAIL(entity.getMsg());
        }
        redis.opsForValue().set(createCacheKeyCaptchaToken(robotWrapper.getId()), entity.getData().getCaptchaToken(), Duration.ofMinutes(5));
        return ResponseResult.SUCCESS(entity.getData().getImg());
    }

    @Override
    public IActionEnum getActionEnum() {
        return CommonActionEnum.IMAGE_CODE;
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
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }*/
}
