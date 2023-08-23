package com.backend.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class FcmConfig {

//    @Value("${fcm.certification}")
//    private String googleApplicationCredentials;
//
//    @Bean
//    public FirebaseMessaging firebaseMessaging() throws IOException {
//
//        ClassPathResource resource = new ClassPathResource(googleApplicationCredentials);
//
//        InputStream in = resource.getInputStream();
//
//        FirebaseApp firebaseApp = null;
//        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();
//
//        if(firebaseAppList != null && !firebaseAppList.isEmpty())
//        {
//            for (FirebaseApp app : firebaseAppList) {
//                if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
//                {
//                    firebaseApp = app;
//                }
//            }
//        }else {
//            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(in)).build();
//
//            firebaseApp = FirebaseApp.initializeApp(options);
//        }
//
//        return FirebaseMessaging.getInstance(firebaseApp);
//    }

}
