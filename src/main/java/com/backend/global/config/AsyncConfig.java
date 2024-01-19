package com.backend.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    /*
    @Async를 사용할때 ThreadPoolTaskExecutor를 설정하지 않으면 쓰레드 1개로 동작합니다.
    따라서 쓰레드 풀을 할당해서 멀티 쓰레드로 동작하도록 만들었습니다.
    executor.setPrestartAllCoreThreads(false)로 설정해서 초반에 요청이 들어올때마다 CorePoolSize까지 쓰레드 개수를 늘리도록 만들었습니다.
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
