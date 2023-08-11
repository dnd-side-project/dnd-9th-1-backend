package com.backend.user.domain;

public enum SocialType {
    KAKAO,
    APPLE;

    public boolean isSameAs(SocialType socialType){
        return this.equals(socialType);
    }
}