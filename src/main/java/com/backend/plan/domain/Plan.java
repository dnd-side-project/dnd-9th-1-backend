package com.backend.plan.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "plan")
public class Plan {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final LocalDateTime MIN_DATE_TIME = LocalDateTime.of(1000, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(9999, 12, 31, 11, 59, 59, 999999000);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId; // 상위 목표를 작성한 사용자의 ID

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "plan_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PlanStatus planStatus;

    @Column(name = "detail_plan_count", nullable = false)
    private Integer detailPlanCount;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "end_date_time_exists", nullable = false)
    private Boolean endDateTimeExists;

    @Column(name = "reminder_enabled", nullable = false)
    private Boolean reminderEnabled;

    @Column(name = "has_retrospect", nullable = false)
    private Boolean hasRetrospect;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;


    @PrePersist
    private void init()
    {
        deleted = Boolean.FALSE;
        hasRetrospect = Boolean.FALSE;
        detailPlanCount = 0;
        planStatus = PlanStatus.PROCESS;
    }

    public Plan(final Long userId, final String title, final LocalDateTime startDateTime, final LocalDateTime endDateTime,
                final Boolean endDateTimeExists, final Boolean reminderEnabled)
    {
        validateTitleLength(title);
        validatePeriod(startDateTime, endDateTime);
        this.userId = userId;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.endDateTimeExists = endDateTimeExists;
        this.reminderEnabled = reminderEnabled;
    }

    private void validateTitleLength(final String title) {

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(String.format("일정 제목의 길이는 %d을 초과할 수 없습니다.", MAX_TITLE_LENGTH));
        }
    }

    private void validatePeriod(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("종료일시가 시작일시보다 이전일 수 없습니다.");
        }
        if (isNotValidDateTimeRange(startDateTime) || isNotValidDateTimeRange(endDateTime)) {
            throw new IllegalArgumentException(
                    String.format("일정은 %s부터 %s까지 등록할 수 있습니다.",
                            MIN_DATE_TIME.toLocalDate(), MAX_DATE_TIME.toLocalDate())
            );
        }
    }

    private boolean isNotValidDateTimeRange(final LocalDateTime dateTime) {
        return dateTime.isBefore(MIN_DATE_TIME) || dateTime.isAfter(MAX_DATE_TIME);
    }
}
