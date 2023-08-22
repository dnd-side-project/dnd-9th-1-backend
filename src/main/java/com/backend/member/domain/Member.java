package com.backend.member.domain;

import com.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private Boolean enabledPush;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private Member (
            final String uid,
            final Provider provider,
            final Boolean enabledPush,
            final MemberStatus memberStatus,
            final Role role
    ) {
        this.enabledPush = enabledPush;
        this.provider = provider;
        this.uid = uid;
        this.memberStatus = memberStatus;
        this.role = role;
    }

    public static Member from(Provider provider, String uid){
        return Member.builder()
                .uid(uid)
                .provider(provider)
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.ROLE_USER)
                .build();
    }

    @PrePersist
    private void setting(){
        this.enabledPush = false;
        this.memberStatus = MemberStatus.ACTIVE;
    }

    public void withdraw() {
        this.memberStatus = MemberStatus.DELETE;
    }
}
