package com.backend.auth.application;

import com.backend.auth.application.client.OAuthClient;
import com.backend.auth.application.client.OAuthClientFactory;
import com.backend.auth.application.dto.OAuthUserInfo;
import com.backend.auth.presentation.dto.LoginRequest;
import com.backend.auth.presentation.dto.LoginResponse;
import com.backend.global.jwt.JwtProvider;
import com.backend.user.application.UserService;
import com.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private final UserService userService;
    private final OAuthClientFactory oAuthClientFactory;
    private final JwtProvider jwtProvider;
    
    public LoginResponse login(LoginRequest loginRequest) {
        OAuthClient oAuthClient = oAuthClientFactory.create(loginRequest.provider());
        OAuthUserInfo userInfo = oAuthClient.getUserInfo(loginRequest.accessToken());

        User uncheckedUser = User.from(userInfo, loginRequest.provider());
        User user = userService.findUserOrRegister(uncheckedUser);

        return new LoginResponse(jwtProvider.generateToken(user), user.getNickname());
    }
}
