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
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {

    private final GoalQueryRepository goalQueryRepository;

    private final DetailGoalQueryRepository detailGoalQueryRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void storeOutDateGoal() {

        log.info("scheduler activated...");
        List<Goal> goalList = goalQueryRepository.findGoalListEndDateExpired(LocalDate.now());
        log.info("{} goal move to store", goalList.size());
        goalList.forEach(Goal::store);
    }

    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    public void sendAlarm()
    {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        List<DetailGoalAlarmResponse> detailGoalAlarmList = detailGoalQueryRepository.getMemberIdListDetailGoalAlarmTimeArrived(today);

        detailGoalAlarmList.forEach(alarmDto -> applicationEventPublisher.publishEvent(new AlarmEvent(alarmDto.memberId(),alarmDto.detailGoalTitle())));
    }
}
