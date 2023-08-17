package com.backend.goal.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.entity.BaseEntity;
import com.backend.global.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "goal")
public class Goal extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 15;
    private static final LocalDate MIN_DATE = LocalDate.of(1000, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId; // 상위 목표를 작성한 사용자의 ID

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "goal_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GoalStatus goalStatus;

    @Column(name = "entire_detail_goal_cnt", nullable = false)
    private Integer entireDetailGoalCnt;

    @Column(name = "completed_detail_goal_cnt", nullable = false)
    private Integer completedDetailGoalCnt;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reminder_enabled", nullable = false)
    private Boolean reminderEnabled;

    @Column(name = "has_retrospect", nullable = false)
    private Boolean hasRetrospect;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void remove()
    {
        this.isDeleted = Boolean.TRUE;
    }


    @PrePersist
    private void init()
    {
        isDeleted = Boolean.FALSE;
        hasRetrospect = Boolean.FALSE;
        entireDetailGoalCnt = 0;
        completedDetailGoalCnt = 0;
        goalStatus = GoalStatus.PROCESS;
    }


    public void increaseEntireDetailGoalCnt()
    {
        this.entireDetailGoalCnt +=1;
    }

    public void decreaseEntireDetailGoalCnt()
    {
        if(entireDetailGoalCnt < 1)
        {
            throw new BusinessException(ErrorCode.ENTIRE_DETAIL_GOAL_CNT_INVALID);
        }

        this.entireDetailGoalCnt -=1;
    }


    public void increaseCompletedDetailGoalCnt()
    {
        this.completedDetailGoalCnt +=1;
    }

    public void decreaseCompletedDetailGoalCnt()
    {
        if(completedDetailGoalCnt < 1)
        {
            throw new BusinessException(ErrorCode.COMPLETED_DETAIL_GOAL_CNT_INVALID);
        }

        this.completedDetailGoalCnt -=1;
    }

    public boolean checkGoalCompleted()
    {
        return completedDetailGoalCnt == entireDetailGoalCnt;
    }
    

    public void update(final String title, final LocalDate startDate, final LocalDate endDate, final Boolean reminderEnabled)
    {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminderEnabled = reminderEnabled;
    }

    public Goal(final Long memberId, final String title, final LocalDate startDate, final LocalDate endDate, final Boolean reminderEnabled)
    {
        validateTitleLength(title);
        validatePeriod(startDate, endDate);
        this.memberId = memberId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reminderEnabled = reminderEnabled;
    }

    private void validateTitleLength(final String title) {

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(String.format("상위 목표 제목의 길이는 %d을 초과할 수 없습니다.", MAX_TITLE_LENGTH));
        }
    }

    private void validatePeriod(final LocalDate startDate, final LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("종료일시가 시작일시보다 이전일 수 없습니다.");
        }

        if (isNotValidDateTimeRange(startDate) || isNotValidDateTimeRange(endDate)) {
            throw new IllegalArgumentException(String.format("상위 목표는 %s부터 %s까지 등록할 수 있습니다.", MIN_DATE, MAX_DATE)
            );
        }
    }

    public Long calculateDday(LocalDate now)
    {
        if(now.isAfter(endDate))
        {
            throw new IllegalArgumentException("현재 일자가 종료 일자보다 뒤라면 d-day를 구할 수 없습니다.");
        }

        return ChronoUnit.DAYS.between(now, endDate);
    }

    private boolean isNotValidDateTimeRange(final LocalDate date) {
        return date.isBefore(MIN_DATE) || date.isAfter(MAX_DATE);
    }
}
