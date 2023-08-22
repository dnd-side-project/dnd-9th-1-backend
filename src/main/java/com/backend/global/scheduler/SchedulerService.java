package com.backend.global.scheduler;

import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;
import com.backend.detailgoal.domain.AlarmEvent;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.domain.DetailGoalQueryRepository;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.GoalQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {

    private final GoalQueryRepository goalQueryRepository;

    private final DetailGoalQueryRepository detailGoalQueryRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int RAND_COUNT = 10;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void storeOutDateGoal() {

        List<Goal> goalList = goalQueryRepository.findGoalListEndDateExpired(LocalDate.now());
        goalList.forEach(Goal::store);
    }

    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Seoul")
    public void sendAlarm()
    {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        LocalTime localTime = LocalTime.now();
        LocalTime now = LocalTime.of(localTime.getHour(), localTime.getMinute(), 0);

        List<DetailGoalAlarmResponse> detailGoalAlarmList = detailGoalQueryRepository.getMemberIdListDetailGoalAlarmTimeArrived(dayOfWeek, now);
        detailGoalAlarmList.forEach(alarmDto ->
                applicationEventPublisher.publishEvent(new AlarmEvent(alarmDto.uid(), alarmDto.detailGoalTitle())));
    }


}
