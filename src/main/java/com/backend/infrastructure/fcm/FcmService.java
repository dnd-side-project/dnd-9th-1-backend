package com.backend.infrastructure.fcm;

import com.backend.auth.application.FcmTokenService;
import com.backend.global.exception.BusinessException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FcmService {



    private final FcmTokenService fcmTokenService;

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessage(String uid, String detailGoalTitle)
    {
        try {
            String fcmToken = fcmTokenService.findFcmToken(uid);
        }
        catch (BusinessException e)
        {
            System.out.println("error");
        }



        Notification notification = Notification.builder()
                .setTitle(PushWord.PUSH_TITLE)
                .setBody(detailGoalTitle + PushWord.PUSH_CONTENT)
                .build();

        Message message = Message.builder()
                .setToken("example")
                .setNotification(notification)
                .build();

        try {
            System.out.println("보낼 예정");
            String send = firebaseMessaging.send(message);
            System.out.println(send);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
