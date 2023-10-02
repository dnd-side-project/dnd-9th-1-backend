package com.backend.detailgoal.presentation;

import com.backend.detailgoal.application.DetailGoalService;
import com.backend.detailgoal.application.dto.response.DetailGoalListResponse;
import com.backend.detailgoal.application.dto.response.DetailGoalResponse;
import com.backend.detailgoal.application.dto.response.GoalCompletedResponse;
import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.presentation.dto.request.DetailGoalSaveRequest;
import com.backend.detailgoal.presentation.dto.request.DetailGoalUpdateRequest;
import com.backend.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import static com.backend.global.common.code.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "detailGoal", description = "하위 목표 API")
public class DetailGoalController {

    private final DetailGoalService detailGoalService;

    @Operation(summary = "하위 목표 리스트 조회", description = "하위 목표 리스트를 조회하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : SELECT_SUCCESS")
    @GetMapping("/goals/{id}/detail-goals")
    public ResponseEntity<CustomResponse<List<DetailGoalListResponse>>> getDetailGoalList(@Parameter(description = "상위 목표 ID") @PathVariable Long id)
    {
        return CustomResponse.success(SELECT_SUCCESS, detailGoalService.getDetailGoalList(id));
    }

    @Operation(summary = "하위 목표 상세 조회", description = "하위 목표를 상세 조회하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : SELECT_SUCCESS")
    @GetMapping("/detail-goals/{id}")
    public ResponseEntity<CustomResponse<DetailGoalResponse>> getDetailGoal(@Parameter(description = "하위 목표 ID") @PathVariable Long id)
    {
        return CustomResponse.success(SELECT_SUCCESS, detailGoalService.getDetailGoal(id));
    }

    @Operation(summary = "하위 목표 생성", description = "하위 목표를 생성하는 API 입니다.")
    @ApiResponse(responseCode = "201", description = "code : 201, message : INSERT_SUCCESS")
    @PostMapping("/goals/{id}/detail-goals")
    public ResponseEntity<CustomResponse<Long>> saveDetailGoal(@Parameter(description = "상위 목표 ID") @PathVariable Long id, @RequestBody @Valid DetailGoalSaveRequest detailGoalSaveRequest)
    {
        DetailGoal detailGoal = detailGoalService.saveDetailGoal(id, detailGoalSaveRequest);
        return CustomResponse.success(INSERT_SUCCESS,detailGoal.getId());
    }

    @Operation(summary = "하위 목표 수정", description = "하위 목표를 수정하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : UPDATE_SUCCESS")
    @PatchMapping("/detail-goals/{id}")
    public ResponseEntity<CustomResponse<Set<DayOfWeek>>> updateDetailGoal(@Parameter(description = "하위 목표 ID") @PathVariable Long id, @RequestBody @Valid DetailGoalUpdateRequest detailGoalUpdateRequest)
    {
        Set<DayOfWeek> dayOfWeeks = detailGoalService.updateDetailGoal(id, detailGoalUpdateRequest);
        return CustomResponse.success(UPDATE_SUCCESS, dayOfWeeks);
    }

    @Operation(summary = "하위 목표 삭제", description = "하위 목표를 삭제하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : DELETE_SUCCESS")
    @DeleteMapping("/detail-goals/{id}")
    public ResponseEntity<CustomResponse<GoalCompletedResponse>> removeDetailGoal(@Parameter(description = "하위 목표 ID") @PathVariable Long id)
    {

        return CustomResponse.success(DELETE_SUCCESS, detailGoalService.removeDetailGoal(id));
    }

    @Operation(summary = "하위 목표 달성", description = "하위 목표를 달성하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : UPDATE_SUCCESS")
    @PatchMapping("/detail-goals/{id}/complete")
    public ResponseEntity<CustomResponse<GoalCompletedResponse>> completeDetailGoal(@Parameter(description = "하위 목표 ID") @PathVariable Long id)
    {
        return CustomResponse.success(UPDATE_SUCCESS, detailGoalService.completeDetailGoal(id));
    }

    @Operation(summary = "하위 목표 달성 취소", description = "하위 목표 달성을 취소하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "code : 200, message : DELETE_SUCCESS")
    @PatchMapping("/detail-goals/{id}/incomplete")
    public ResponseEntity<CustomResponse<GoalCompletedResponse>> incompleteDetailGoal(@Parameter(description = "하위 목표 ID") @PathVariable Long id)
    {
        detailGoalService.inCompleteDetailGoal(id);
        return CustomResponse.success(UPDATE_SUCCESS);
    }
}
