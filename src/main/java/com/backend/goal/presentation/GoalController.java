package com.backend.goal.presentation;

import com.backend.global.common.response.CustomResponse;
import com.backend.goal.application.GoalService;
import com.backend.goal.application.dto.response.GoalCountResponse;
import com.backend.goal.application.dto.response.GoalListResponse;
import com.backend.goal.application.dto.response.GoalResponse;
import com.backend.goal.application.dto.response.RetrospectEnabledGoalCountResponse;
import com.backend.goal.domain.repository.GoalListResponseDto;
import com.backend.goal.presentation.dto.GoalRecoverRequest;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import com.backend.goal.presentation.dto.GoalUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.backend.global.common.code.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goals")
@Tag(name = "goal", description = "상위 목표 API")
public class GoalController {

    private final GoalService goalService;

    @Operation(summary = "상위 목표 리스트 조회", description = "상위 목표 리스트를 조회하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : SELECT_SUCCESS")
    @GetMapping
    public ResponseEntity<CustomResponse<GoalListResponse>> getGoalList(
                                                       @AuthenticationPrincipal UserDetails userDetails,
                                                       @Parameter(hidden = true) @PageableDefault(size = 10) Pageable pageable,
                                                       @Parameter(description = "처음 조회 시 Null 전달, 이후부터는 이전 응답 데이터 중 마지막 ID를 전달") @RequestParam(required = false) Long lastId,
                                                       @Parameter(description = "store(보관함), process(채움함), complete(완료함) 중 하나로 호출") @RequestParam String goalStatus)
    {
        return CustomResponse.success(SELECT_SUCCESS,goalService.getGoalList(userDetails.getUsername(),lastId,pageable,goalStatus));
    }


    @Operation(summary = "상위 목표 상태별 개수 조회", description = "상위 목표 상태별 개수를 조회하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : SELECT_SUCCESS")
    @GetMapping("/count")
    public ResponseEntity<CustomResponse<GoalCountResponse>> getGoalCounts(@AuthenticationPrincipal UserDetails userDetails)
    {
        return CustomResponse.success(SELECT_SUCCESS,goalService.getGoalCounts(userDetails.getUsername()));
    }

    @Operation(summary = "회고 작성 가능한 목표 개수 조회", description = "회고 작성 가능한 목표 개수를 조회하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : SELECT_SUCCESS")
    @GetMapping("/retrospect-enabled/count")
    public ResponseEntity<CustomResponse<RetrospectEnabledGoalCountResponse>> getRetrospectEnabledGoalCount(@AuthenticationPrincipal UserDetails userDetails)
    {
        return CustomResponse.success(SELECT_SUCCESS,goalService.getGoalCountRetrospectEnabled(userDetails.getUsername()));
    }

    @Operation(summary = "회고 완료 후 보관함 내 목표 추천", description = "보관함에 들어있는 목표들 중 랜덤하게 3개를 추천해줍니다")
    @ApiResponse(responseCode = "200", description = "code : 200, message : SELECT_SUCCESS")
    @GetMapping("/stored-goals")
    public ResponseEntity<CustomResponse<List<GoalListResponseDto>>> getStoredGoalList(@AuthenticationPrincipal UserDetails userDetails)
    {
        return CustomResponse.success(SELECT_SUCCESS,goalService.getStoredGoalList(userDetails.getUsername()));
    }


    @Operation(summary = "상위 목표 삭제", description = "상위 목표를 삭제하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : DELETE_SUCCESS")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> removeGoal(@Parameter(description = "상위 목표 ID") @PathVariable Long id)
    {
        goalService.removeGoal(id);
        return CustomResponse.success(DELETE_SUCCESS);
    }

    @Operation(summary = "보관함 내 상위 목표 복구", description = "보관함에 들어간 상위 목표를 복구하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : UPDATE_SUCCESS")
    @PatchMapping("/{id}/recover")
    public ResponseEntity<CustomResponse<Void>> recoverGoal(@Parameter(description = "상위 목표 ID") @PathVariable Long id, @RequestBody @Valid GoalRecoverRequest goalRecoverRequest)
    {
        goalService.recoverGoal(id, goalRecoverRequest);
        return CustomResponse.success(UPDATE_SUCCESS);
    }


    @Operation(summary = "상위 목표 수정", description = "상위 목표를 수정하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : UPDATE_SUCCESS")
    @PatchMapping("/{id}")
    public ResponseEntity<CustomResponse<GoalResponse>> updateGoal(@Parameter(description = "상위 목표 ID") @PathVariable Long id, @RequestBody @Valid GoalUpdateRequest goalSaveRequest)
    {
        return CustomResponse.success(UPDATE_SUCCESS, goalService.updateGoal(id, goalSaveRequest));
    }

    @Operation(summary = "상위 목표 생성", description = "상위 목표를 생성하는 API 입니다.")
    @ApiResponse(responseCode = "201", description = "code : 201, message : INSERT_SUCCESS")
    @PostMapping
    public ResponseEntity<CustomResponse<Void>> saveGoal(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid GoalSaveRequest goalSaveRequest)
    {
        // 아직 유저 식별 값으로 뭐가 들어올지 몰라 1L로 설정해놨습니다.
        goalService.saveGoal(userDetails.getUsername(), goalSaveRequest);
        return CustomResponse.success(INSERT_SUCCESS);
    }


}
