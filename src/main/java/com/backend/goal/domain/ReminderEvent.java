package com.backend.goal.domain;

public record ReminderEvent(

        Long memberId,
        String goalTitle

) {
}
