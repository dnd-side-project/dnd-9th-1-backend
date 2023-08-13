package com.backend.member.domain;

public enum SocialType {
    KAKAO,
    APPLE;

    public boolean isSameAs(SocialType socialType){
        return this.equals(socialType);
    }
}