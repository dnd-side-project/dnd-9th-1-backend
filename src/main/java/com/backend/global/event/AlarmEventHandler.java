package com.backend.global.event;

import com.backend.detailgoal.domain.AlarmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlarmEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendAlarm(AlarmEvent event) {

        // FCMService 호출 후, 알림 보내는 로직 구현 예정
    }
}
