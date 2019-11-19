package com.robot.center.project.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author dongchuan
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncTaskConfig implements AsyncConfigurer {
    /**
     * ThredPoolTaskExcutor的处理流程 当池子大小小于corePoolSize，就新建线程，并处理请求
     * 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
     * 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝处理
     * 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁
     */
    @Override
    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 最小线程数(核心线程数)
        taskExecutor.setCorePoolSize(10);
        // 最大线程数
        taskExecutor.setMaxPoolSize(300);
        // 等待队列(队列最大长度)
        taskExecutor.setQueueCapacity(1000);
        // 线程池维护线程所允许的空闲时间 ，单位s
        taskExecutor.setKeepAliveSeconds(300);
        // 线程池对拒绝任务(无线程可用)的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //任务超时时间，超过这个时间就会强制销毁
        taskExecutor.setAwaitTerminationSeconds(60);
 
        taskExecutor.initialize();
        log.info("线程池初始化完成...");
        return taskExecutor;
    }
 
    /**
     * 异步异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }
 
    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            log.error("异步错误", throwable);
        }
    }
}
