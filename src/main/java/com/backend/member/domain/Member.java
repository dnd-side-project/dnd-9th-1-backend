package com.backend.member.domain;

import com.backend.auth.application.dto.response.OAuthMemberInfo;
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
@Table(name="member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
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
    private MemberStatus memberStatus;

    @Builder
    private Member(
            final String nickname,
            final Boolean enabledPush,
            final SocialType socialType,
            final String socialId,
            final MemberStatus memberStatus
    ) {
        this.nickname = nickname;
        this.enabledPush = enabledPush;
        this.socialType  = socialType;
        this.socialId = socialId;
        this.memberStatus = memberStatus;
    }

    public static Member from(OAuthMemberInfo memberInfo, String provider) {
        return Member.builder()
                .nickname(memberInfo.nickname())
                .socialType(SocialType.valueOf(provider.toUpperCase(Locale.ROOT)))
                .socialId(memberInfo.id())
                .memberStatus(MemberStatus.ACTIVE)
                .build();
    }

    @PrePersist
    private void setting(){
        this.enabledPush = false;
        this.memberStatus = MemberStatus.ACTIVE;
    }
}
