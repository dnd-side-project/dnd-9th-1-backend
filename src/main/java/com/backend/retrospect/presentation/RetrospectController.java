package com.backend.retrospect.presentation;

import com.backend.global.common.response.CustomResponse;
import com.backend.retrospect.application.RetrospectService;
import com.backend.retrospect.application.dto.response.RetrospectResponse;
import com.backend.retrospect.presentation.dto.request.RetrospectSaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.global.common.code.SuccessCode.*;

@Tag(name = "retrospect", description = "회고 작성, 조회, 삭제 API입니다.")
@RestController
@RequiredArgsConstructor
public class RetrospectController {
    private final RetrospectService retrospectService;
    @Operation(summary = "회고 작성", description = "완료함의 상위 목표에 대한 회고를 작성합니다.")
    @PostMapping("/goal/{goal_id}/retrospect")
    public ResponseEntity<CustomResponse<Long>> saveRetrospect(
            @Parameter(description = "상위 목표 아이디") @PathVariable(value = "goal_id") Long goalId,
            @RequestBody RetrospectSaveRequest saveRequest) {
        return CustomResponse.success(INSERT_SUCCESS,
                retrospectService.saveRetrospect(goalId, saveRequest.hasGuide(), saveRequest.contents(), saveRequest.successLevel()));
    }

    @Operation(summary = "회고 조회", description = "완료함의 상위 목표에 대한 회고를 조회합니다.")
    @GetMapping("/goal/{goal_id}/retrospect")
    public ResponseEntity<CustomResponse<RetrospectResponse>> getRetrospect (
            @Parameter(description = "상위 목표 아이디") @PathVariable(value = "goal_id") Long goalId) {
        return CustomResponse.success(SELECT_SUCCESS, retrospectService.getRetrospect(goalId));
    }
}
