package com.backend.infrastructure.fcm;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.backend.auth.application.FcmTokenService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmTokenService fcmTokenService;

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessage(String uid, String detailGoalTitle) {
        String fcmToken = fcmTokenService.findFcmToken(uid);

        if (Objects.isNull(fcmToken)) {
            return;
        }

        Notification notification = Notification.builder()
            .setTitle(PushWord.PUSH_TITLE)
            .setBody(detailGoalTitle + PushWord.PUSH_CONTENT)
            .build();

        Message message = Message.builder()
            .setToken(fcmToken)
            .setNotification(notification)
            .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
