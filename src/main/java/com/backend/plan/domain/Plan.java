package com.backend.plan.domain;

import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "plan")
public class Plan extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 15;
    private static final LocalDate MIN_DATE = LocalDate.of(1000, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

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

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "reminder_enabled", nullable = false)
    private Boolean reminderEnabled;

    @Column(name = "has_retrospect", nullable = false)
    private Boolean hasRetrospect;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public void remove()
    {
        this.deleted = Boolean.TRUE;
    }


    @PrePersist
    private void init()
    {
        deleted = Boolean.FALSE;
        hasRetrospect = Boolean.FALSE;
        detailPlanCount = 0;
        planStatus = PlanStatus.PROCESS;
    }

    public Plan(final Long userId, final String title, final LocalDate startDate, final LocalDate endDate, final Boolean reminderEnabled)
    {
        validateTitleLength(title);
        validatePeriod(startDate, endDate);
        this.userId = userId;
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
            throw new IllegalArgumentException(
                    String.format("상위 목표는 %s부터 %s까지 등록할 수 있습니다.",
                            MIN_DATE, MAX_DATE)
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
