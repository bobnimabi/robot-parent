package com.robot.bbin.activity.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 系统日志切面
 */
@Slf4j
@Aspect  // 使用@Aspect注解声明一个切面
@Component
public class SysLogAspect {

    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
    @Pointcut("execution(public * com.robot.center.controller.RobotControllerBase+.*(..))")
    public void logPointCut() {}

    /**
     * 环绕通知 @Around  ， 当然也可以使用 @Before (前置通知)  @After (后置通知)
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        saveLog(point);

        //调用目标方法
        Object result = point.proceed();
        log.info("接口响应：{}",JSON.toJSONString(result));
        return result;
    }

    /**
     * 保存日志
     */
    private void saveLog(ProceedingJoinPoint joinPoint) {
        //请求的参数
        Object[] args = joinPoint.getArgs();
        log.info("接口请求:"+JSON.toJSONString(args));
    }
    private String getMethod(ProceedingJoinPoint joinPoint) {
        try {
            Signature sig = joinPoint.getSignature();
            MethodSignature msig = null;
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            msig = (MethodSignature) sig;
            Object target = joinPoint.getTarget();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
           return currentMethod.getName();
        } catch (Exception e) {

        }
        return "test";
    }
}
