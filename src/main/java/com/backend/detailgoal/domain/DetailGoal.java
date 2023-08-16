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


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "detail_goal")
public class DetailGoal extends BaseEntity {

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

    @ElementCollection
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
