package com.backend.auth.presentation;

import com.backend.auth.jwt.TokenProvider;
import com.backend.auth.presentation.dto.request.ExpireTimeRequest;
import com.backend.auth.presentation.dto.response.ExpireTimeResponse;
import com.backend.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.global.common.code.SuccessCode.SELECT_SUCCESS;
import static com.backend.global.common.code.SuccessCode.UPDATE_SUCCESS;

@Tag(name = "token", description = "토큰 관리 API입니다.")
@RequiredArgsConstructor
@RequestMapping("/token")
@RestController
public class TokenController {
    private final TokenProvider tokenProvider;

    @Operation(summary = "액세스 토큰 유효 시간 수정", description = "액세스 토큰의 유효 시간을 수정한다.")
    @PutMapping("/access/expire")
    public ResponseEntity<CustomResponse<Void>> updateAccessTokenExpireTime(@RequestBody ExpireTimeRequest expireTimeRequest){
        tokenProvider.updateAccessTokenExpireTime(expireTimeRequest.toLong());
        return CustomResponse.success(UPDATE_SUCCESS);
    }

    @Operation(summary = "리프레시 토큰 유효 시간 수정", description = "리프레시 토큰의 유효 시간을 수정한다.")
    @PutMapping("/refresh/expire")
    public ResponseEntity<CustomResponse<Void>> updateRefreshTokenExpireTime(@RequestBody ExpireTimeRequest expireTimeRequest){
        tokenProvider.updateRefreshTokenExpireTime(expireTimeRequest.toLong());
        return CustomResponse.success(UPDATE_SUCCESS);
    }

    @Operation(summary = "액세스 토큰 유효 시간 조회", description = "액세스 토큰의 유효 시간을 초 단위로 조회한다.")
    @GetMapping("/access/expire")
    public ResponseEntity<CustomResponse<ExpireTimeResponse>> getAccessTokenExpireTime(){
        return CustomResponse.success(SELECT_SUCCESS, ExpireTimeResponse.from(tokenProvider.getAccessTokenExpireTime()));
    }

    @Operation(summary = "리프레시 토큰 유효 시간 조회", description = "리프레시 토큰의 유효 시간을 초 단위로 조회한다.")
    @GetMapping("/refresh/expire")
    public ResponseEntity<CustomResponse<ExpireTimeResponse>> getRefreshTokenExpireTime(){
        return CustomResponse.success(SELECT_SUCCESS, ExpireTimeResponse.from(tokenProvider.getRefreshTokenExpireTime()));
    }

}
