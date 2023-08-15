package com.backend.detailgoal.domain;

import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


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

    @Column(name = "title")
    private String title;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "alarm_enabled")
    private Boolean alarmEnabled;

    @ElementCollection
    @CollectionTable(name = "detail_goals_alarm_days", joinColumns = @JoinColumn(name = "detail_goal_id"))
    @Column(name = "alarm_days")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> alarmDays = new HashSet<>();

    @Column(name = "alarm_time")
    private LocalTime alarmTime;

    public void complete()
    {
        this.isCompleted = Boolean.TRUE;
    }

    public void inComplete()
    {
        this.isCompleted = Boolean.FALSE;
    }
}
