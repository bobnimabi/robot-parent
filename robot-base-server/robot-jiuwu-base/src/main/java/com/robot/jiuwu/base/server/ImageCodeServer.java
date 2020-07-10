package com.robot.jiuwu.base.server;



import com.robot.center.constant.RobotConsts;
import com.robot.code.response.Response;
import com.robot.core.common.TContext;
import com.robot.core.function.base.*;

import com.robot.core.robot.manager.RobotWrapper;

import com.robot.jiuwu.base.common.Constant;
import com.robot.jiuwu.base.function.ImageCodeFunction;
import com.robot.jiuwu.base.vo.ImageCodeResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


/**
 * Created by mrt on 11/14/2019 8:06 PM
 * 图片验证码
 */
@Slf4j
@Service
public class ImageCodeServer implements IAssemFunction<Object> {

    @Autowired
    private ImageCodeFunction ImageCodeFunction;

    @Autowired
    private StringRedisTemplate redis;

    @Override
    public Response doFunction(ParamWrapper<Object> paramWrapper, RobotWrapper robotWrapper) throws Exception {
        Object imageCodeDTO = paramWrapper.getObj();

        Response<ImageCodeResultVO>  imageResponse = ImageCodeFunction.doFunction(new ParamWrapper<>(imageCodeDTO),robotWrapper);
        if (!imageResponse.isSuccess()) {
            return imageResponse;
        }

        ImageCodeResultVO imageVO = imageResponse.getObj();
        if (!Constant.SUCCESS.equals(imageVO.getCode())) {
            return Response.FAIL(imageVO.getMsg());
        }

        String captchaToken = imageVO.getData().getCaptchaToken();

        redis.opsForValue().set(createCacheKeyCaptchaToken(robotWrapper.getId()),captchaToken , Duration.ofMinutes(5));  //imageVO.getData().getCaptchaToken()
        String s = redis.opsForValue().get(createCacheKeyCaptchaToken(robotWrapper.getId()));
        return Response.SUCCESS(imageVO.getData().getImg());
    }

    // 创建图片验证码的缓存标志
  public   static String createCacheKeyCaptchaToken(long robotId) {
        return new StringBuilder(50)
                .append(RobotConsts.CAPTCHA_TOKEN)
                .append(TContext.getTenantId()).append(":")
                .append(TContext.getChannelId()).append(":")
                .append(TContext.getPlatformId()).append(":")
                .append(TContext.getFunction()).append(":")
                .append(robotId)
                .toString();
    }




/*
    public static void main(String[] args) {
        String str="iVBORw0KGgoAAAANSUhEUgAAAGQAAAAjCAIAAADt3TjXAAAEsUlEQVR42u2Zf0hTXRjHhSSKyJd6k8zmGG1K79asWL8mJRZMSkHRfviPFER/hBUoFixK/9h/YYEQoWNgFEL+8/6h1BR5E972WpAZRJIVRCCZha5aS82JPj23c7jz3b33dO/umTXz4WGMncP37nz23HO/51kKJHOsUx319fX6L5cCCzeCwaDRaCSwBgcHF2Gxwuv1ElLFxcVcBBcyrIKCAgKrtbV1ERYr+vv7CSmz2RwOhxdhsaK2tpbAqq6u5qU5T7BGR0cDgYDP58M14A5isVjE51QiLjc+Pp6dnU30+/r6kgPW9MOrJNetSftfzolEXLetrY2I5+fnc5Sdp8qKtT1zwIlAGan1ciUlJeQ6TU1NyQfLZrOVl5e73e6Wlpbe3t6xsTFNlaUGqJjP/64nymiy8EIKFgwQY2kpbNkCRqPwiu+bm+HDh3hh4ULUpM5a4/7DeDweUrPHS/NkK9Tvh02b5NeCn3d1/TawIpGI3W4nyj09PdIJSMpgYC0HR7u7fw9Yfr+fyDocjpmZmZjRd+/AbKZfOzcXbtyA4WGYmoI3b+D6dbDb6ZDFAu/fq4B12tKBGQMroVs+X9nKykoi29DQIB2tqaEr2rZNACdF6XDQCefOqa4sgmxF6iRmEsEaGRkxGAyomZmZOTQ0FDP65QuYTJRFICCvcO8enbBhA5o1dbBelG0WKwt5EXZixf2ysBobG4lmRUWFdLS9na6osJAlgqNkWkeH6j0LeUlvQ47UuMOanZ11Op1Esx3BSOLCBbqcy5dZOjhKptXV6YMlpRY3OO6w8DhFBK1W6xRu2pI4fJguR+4hGQ0cJdOOHIkL1tmzsGePcBtjbt0qqFy5IjxB9IDjDquqqooI1klL4nts306X8/o1SwdHybSdO7VYh/Rlo2w/4nbDxIR8xc0zrFAoZDKZ2E3RjRvpNw+F2FJ0mtXKDxZJlws+f47nPuULC09RRK2oqEhpDh5ryHeORNi2lk7D+RpgOf587PHAnTswMCB4EFRBLk+fgtcLeXlRXkeP/mAlsuD4wnK5XD9siiYWFjEQSornz0d5oT1RGSK1FamrEtei+Qm3IZvXd7tMdeNoRiIm5IXJ174xQusGv2MHV1j371Ndp1OXddBvRNTEoUParANaDZ6wwmGqi6dTXj4rceDiMKUnb09iamj+MXjhaYs7rPiMiJpAV78k/Sum1uOOiEwXrAcPqO6uXdo7ywqh/nmqNcSDNPLKuPXQ9LJb00EaeemChaaBSJ85k1hYvMDFtGiQF8mYFs0fqz+5DnY1d57Qtmcp8ULrcPFi1DrcvTt/sJS6b+p6OELJzG3+vX0rrCXrv3/XXBpYmhPGopvb/ENeYrJgIftTp+DmTXj0CP4pLA4GYXpaqORnz8Dng927o6Tk2iEJiZLCx7KpsZUK69ezziSpGZNrW/qlNynySlHTU2ace/btg48f4eeGEkQlrHgmsdkU/7Do7KSyMTcpq7JiVKS8srIEE4+1lkQhUtu/90nuX0MZ6Z/SVk4sXzaFr/gePzmw94m0YEVqirCGh4VnLW5MZWXCTWdZ+Qrp5OQIjYtjx+DaNZke9gILaWGq/ZOV7U6TInJXp+pU+AbmknU1lEH0sAAAAABJRU5ErkJggg==";


        byte[] s = Base64.decodeBase64(str);
        System.out.println("s = " + s);


        byte[] bytes = Base64.decodeBase64(str);
        System.out.println("bytes = " + bytes);


    }*/



}
