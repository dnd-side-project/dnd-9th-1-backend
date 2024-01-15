package com.backend.global.scheduler;

import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;

import com.backend.detailgoal.domain.event.AlarmEvent;
import com.backend.detailgoal.domain.repository.DetailGoalQueryRepository;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.repository.GoalQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {

    private final GoalQueryRepository goalQueryRepository;

    private final DetailGoalQueryRepository detailGoalQueryRepository;

    private final ApplicationEventPublisher applicationEventPublisher;


    @SchedulerLock(name = "outdate_goal_lock", lockAtMostFor = "10s", lockAtLeastFor = "10s")
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void storeOutDateGoal() {

        List<Goal> goalList = goalQueryRepository.findGoalListEndDateExpired(LocalDate.now());
        goalList.forEach(Goal::store);
    }

    @SchedulerLock(name = "send_alarm_lock", lockAtMostFor = "10s", lockAtLeastFor = "10s")
    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    public void sendAlarm()
    {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        LocalTime localTime = LocalTime.now();
        LocalTime now = LocalTime.of(localTime.getHour(), localTime.getMinute(), 0);

        List<DetailGoalAlarmResponse> detailGoalAlarmList = detailGoalQueryRepository.getMemberIdListDetailGoalAlarmTimeArrived(dayOfWeek, now);
        log.info("{}",detailGoalAlarmList.size());
        detailGoalAlarmList.forEach(alarmDto ->
                applicationEventPublisher.publishEvent(new AlarmEvent(alarmDto.uid(), alarmDto.detailGoalTitle())));
    }
}
