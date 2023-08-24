package com.backend.global.scheduler;

import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;
import com.backend.detailgoal.domain.event.AlarmEvent;
import com.backend.detailgoal.domain.repository.DetailGoalQueryRepository;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.repository.GoalQueryRepository;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SchedulerService {

    private final GoalQueryRepository goalQueryRepository;

    private final DetailGoalQueryRepository detailGoalQueryRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int RAND_COUNT = 2;

    private static final int REMIND_INTERVAL = 14;

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
        log.info("{}",detailGoalAlarmList.size());
        detailGoalAlarmList.forEach(alarmDto ->
                applicationEventPublisher.publishEvent(new AlarmEvent(alarmDto.uid(), alarmDto.detailGoalTitle())));
    }

//    @Scheduled(cron = "0 19 * * 0 *", zone = "Asia/Seoul")
//    public void sendReminder()
//    {
//        List<Goal> goalListReminderEnabled = goalQueryRepository.findGoalListReminderEnabled();
//
//        Random random = new Random();
//
//        // 랜덤하게 2개 선택
//        for (int i = 0; i < RAND_COUNT; i++) {
//
//            int randomIndex = random.nextInt(goalListReminderEnabled.size());
//            Goal goal = goalListReminderEnabled.get(randomIndex);
//
//            if(Objects.nonNull(goal.getLastRemindDate()) && isIntervalDateExpired(goal))
//            {
//                goal.updateLastRemindDate(LocalDate.now());
//                applicationEventPublisher.publishEvent(new ReminderEvent(goal.getMemberId(), goal.getTitle()));
//            }
//        }
//    }
//
//    private boolean isIntervalDateExpired(Goal goal) {
//        return goal.getLastRemindDate().isBefore(LocalDate.now().minusDays(REMIND_INTERVAL));
//    }


}
