package com.backend.global.event;

import com.backend.goal.domain.event.ReminderEvent;
import com.backend.infrastructure.fcm.FcmService;
import com.backend.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReminderEventHandler {

    private final FcmService fcmService;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendAlarm(ReminderEvent event) {

    }
}
