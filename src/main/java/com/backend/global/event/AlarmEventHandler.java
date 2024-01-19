package com.backend.global.event;


import com.backend.detailgoal.domain.event.AlarmEvent;
import com.backend.infrastructure.fcm.FcmService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlarmEventHandler {


    private final FcmService fcmService;

    @Async
    @EventListener
    public void sendAlarm(AlarmEvent event) {
       fcmService.sendMessage(event.uid(), event.detailGoalTitle());
    }
}
