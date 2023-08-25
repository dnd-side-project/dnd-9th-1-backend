package com.backend.detailgoal.domain.event;

public record AlarmEvent(
        String uid,
        String detailGoalTitle
) {


}
