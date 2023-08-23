package com.backend.infrastructure.fcm;

import com.backend.auth.application.FcmTokenService;
import com.backend.global.exception.BusinessException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

//    private final FcmTokenService fcmTokenService;
//
//    private final FirebaseMessaging firebaseMessaging;
//
//    public void sendMessage(String uid, String detailGoalTitle)
//    {
//        String fcmToken = fcmTokenService.findFcmToken(uid);
//
//        if(Objects.isNull(fcmToken))
//        {
//            return;
//        }
//
//        Notification notification = Notification.builder()
//                .setTitle(PushWord.PUSH_TITLE)
//                .setBody(detailGoalTitle + PushWord.PUSH_CONTENT)
//                .build();
//
//        Message message = Message.builder()
//                .setToken("example")
//                .setNotification(notification)
//                .build();
//
//        try {
//
//            log.info("message send start...");
//            String send = firebaseMessaging.send(message);
//            log.info("message send finished, {}", send);
//
//        } catch (FirebaseMessagingException e) {
//            e.printStackTrace();
//        }
//    }
}
