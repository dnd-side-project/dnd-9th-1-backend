package com.backend.detailgoal.domain;

import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "detail_goal")
public class DetailGoal extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_goal_id")
    private Long id;

    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Column(name = "alarm_enabled", nullable = false)
    private Boolean alarmEnabled;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "detail_goals_alarm_days", joinColumns = @JoinColumn(name = "detail_goal_id"))
    @Column(name = "alarm_days")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> alarmDays;

    @Column(name = "alarm_time")
    private LocalTime alarmTime;

    public void setGoalId(Long goalId)
    {
        this.goalId = goalId;
    }

    public void remove()
    {
        this.isDeleted = Boolean.TRUE;
    }


    public void update(String title, Boolean alarmEnabled, LocalTime alarmTime, List<String> alarmDays)
    {
         validateTitleLength(title);
         this.title = title;
         this.alarmEnabled = alarmEnabled;
         this.alarmTime = alarmTime;
         this.alarmDays = alarmDays.stream().map(DayOfWeek::valueOf).collect(Collectors.toList());
    }

    private void validateTitleLength(final String title) {

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(String.format("상위 목표 제목의 길이는 %d을 초과할 수 없습니다.", MAX_TITLE_LENGTH));
        }
    }

    @Builder
    public DetailGoal(Long goalId, String title, Boolean isCompleted, Boolean alarmEnabled, List<DayOfWeek> alarmDays, LocalTime alarmTime) {

        this.goalId = goalId;
        this.title = title;
        this.isCompleted = isCompleted;
        this.alarmEnabled = alarmEnabled;
        this.alarmDays = alarmDays;
        this.alarmTime = alarmTime;
    }

    @PrePersist
    public void init()
    {
        this.isDeleted = Boolean.FALSE;
        this.isCompleted = Boolean.FALSE;
    }

    public void complete()
    {
        this.isCompleted = Boolean.TRUE;
    }

    public void inComplete()
    {
        this.isCompleted = Boolean.FALSE;
    }
}
