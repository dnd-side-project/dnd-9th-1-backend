package com.backend.user.domain;


import com.backend.auth.application.dto.OAuthUserInfo;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false)
    private Boolean enabledPush;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 15)
    private SocialType socialType;

    @Column(nullable = false)
    private String socialId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    @Builder
    private User (
            final String nickname,
            final Boolean enabledPush,
            final SocialType socialType,
            final String socialId,
            final UserStatus userStatus
    ) {
        this.nickname = nickname;
        this.enabledPush = enabledPush;
        this.socialType  = socialType;
        this.socialId = socialId;
        this.userStatus = userStatus;
    }

    public static User from(OAuthUserInfo userInfo, String provider) {
        return User.builder()
                .nickname(userInfo.nickname())
                .socialType(SocialType.valueOf(provider.toUpperCase(Locale.ROOT)))
                .socialId(userInfo.socialId())
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
