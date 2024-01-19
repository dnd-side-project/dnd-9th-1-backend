package com.backend.global.scheduler;

import static com.backend.global.scheduler.SchedulerConstant.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import com.backend.detailgoal.application.dto.response.DetailGoalAlarmResponse;
import com.backend.detailgoal.domain.event.AlarmEvent;
import com.backend.detailgoal.domain.repository.DetailGoalQueryRepository;
import com.backend.goal.domain.Goal;
import com.backend.goal.domain.repository.GoalQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final GoalQueryRepository goalQueryRepository;

    private final DetailGoalQueryRepository detailGoalQueryRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /*
    매일 0시 0분에 달성 기간이 지난 목표들을 보관함으로 이동시킵니다.
    서버가 Scale out되면 스케쥴러가 중복 실행될 수 있으므로 Redis 기반의 스케쥴러 락을 사용해서 하나의 스케쥴러만 실행되도록 만들었습니다.
    RDBMS(MySQL)를 사용할수도 있고, Redis를 사용할 수도 있습니다. 현재 서비스에서 인증 토큰 용도로 Redis를 도입했기 때문에 추가 인프라 비용 없이 Redis를 선택했습니다.
    서비스가 성장해서 스케쥴러의 종류가 많아지고 스케일 아웃으로 서버 수도 증가하면, 스케쥴러 락을 사용하기 보다는 알림 서버를 따로 분리하는 선택을 할 것 같습니다.
    - 현재는 인프라 비용 문제로 AWS 상에서 단일 서버만 운영하고 있습니다.
     */
    @SchedulerLock(name = OUTDATED_GOAL_LOCK)
    @Scheduled(cron = "0 0 * * * *", zone = LOCAL_TIME_ZONE)
    @Transactional
    public void storeOutDateGoal() {

        List<Goal> goalList = goalQueryRepository.findGoalListEndDateExpired(LocalDate.now());
        goalList.forEach(Goal::store);
    }

    /*
    사용자가 지정한 시간(30분 단위)에 목표에 대한 알림을 전송합니다.
     */
    @SchedulerLock(name = SEND_ALARM_LOCK)
    @Scheduled(cron = "0 */30 * * * *", zone = LOCAL_TIME_ZONE)
    public void sendAlarm() {
        List<DetailGoalAlarmResponse> detailGoalAlarmList = detailGoalQueryRepository.getMemberIdListDetailGoalAlarmTimeArrived(
            LocalDate.now().getDayOfWeek(), LocalTime.now());

        detailGoalAlarmList.forEach(alarmDto -> applicationEventPublisher.publishEvent(
            new AlarmEvent(alarmDto.uid(), alarmDto.detailGoalTitle())));
    }
}
