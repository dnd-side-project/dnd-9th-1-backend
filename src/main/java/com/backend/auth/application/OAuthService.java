package com.backend.auth.application;

import com.backend.auth.application.client.OAuthHandler;
import com.backend.auth.application.dto.OAuthUserInfo;
import com.backend.auth.presentation.dto.LoginRequest;
import com.backend.auth.presentation.dto.LoginResponse;
import com.backend.global.util.JwtUtil;
import com.backend.user.application.UserService;
import com.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuthService {

    @Value("${jwt.secret}")
    private String key;

    private Long expireTime = 1000 * 60 * 60L;

    private final UserService userService;
    private final OAuthHandler oAuthHandler;

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        OAuthUserInfo userInfo = oAuthHandler.getUserInfo(loginRequest.accessToken(), loginRequest.provider());

        User uncheckedUser = User.from(userInfo, loginRequest.provider());
        User user = userService.findUserOrRegister(uncheckedUser);

        return new LoginResponse(JwtUtil.generateToken(user, key, expireTime), user.getNickname());
    }
}
