package com.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtLeastFor = "5s", defaultLockAtMostFor = "5s")
public class SchedulerConfig implements SchedulingConfigurer {

    private static final String SCHEDULER_THREAD_POOL_NAME = "scheduler thread pool";
    private static final String THREAD_NAME_PREFIX = "scheduler-thread-";
    private static final int POOL_SIZE = 3;

    /*
    쓰레드풀 사이즈를 선정할때는 필요 이상으로 크게 할당하는걸 경계해야 합니다.
    서비스에 쓰레드 개수가 늘어나는건 쓰레드 간 경합을 증가시켜서 컨텍스트 스위칭 비용을 증가시킵니다.
    따라서 현재 서비스에 스케쥴러가 2개만 존재하고 스케쥴링 간격이 긴 만큼 여유분 1개를 추가해서 총 3개의 쓰레드를 할당했습니다.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadGroupName(SCHEDULER_THREAD_POOL_NAME);
        threadPoolTaskScheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
