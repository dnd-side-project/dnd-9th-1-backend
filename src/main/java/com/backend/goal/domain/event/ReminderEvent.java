package com.backend.goal.domain.event;

public record ReminderEvent(

        Long memberId,
        String goalTitle

) {
}
