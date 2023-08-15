package com.backend.goal.presentation;

import com.backend.global.common.response.CustomResponse;
import com.backend.goal.application.GoalService;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import com.backend.goal.presentation.dto.GoalUpdateRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.global.common.code.SuccessCode.*;

@RestController
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @Tag(name = "상위 목표 리스트 조회", description = "상위 목표 리스트를 조회하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상위 목표 리스트 조회 성공"),
    })
    @GetMapping("/goals")
    public ResponseEntity<CustomResponse> getGoalList(
                                                       @Parameter(hidden = true) @PageableDefault(size = 10) Pageable pageable,
                                                       @RequestParam(required = false) Long lastId,
                                                       @RequestParam String goalStatus)
    {
        return CustomResponse.success(SELECT_SUCCESS,goalService.getGoalList(lastId,pageable,goalStatus));
    }

    @Tag(name = "상위 목표 상태별 개수 조회", description = "상위 목표 상태별 개수를 조회하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상위 목표 상태별 개수 조회 성공"),
    })
    @GetMapping("/goals/count")
    public ResponseEntity<CustomResponse> getGoalCounts()
    {
        return CustomResponse.success(SELECT_SUCCESS,goalService.getGoalCounts());
    }


    @Tag(name = "상위 목표 삭제", description = "상위 목표를 삭제하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상위 목표 삭제 성공"),
    })
    @DeleteMapping("/goals/{id}")
    public ResponseEntity<CustomResponse> removeGoal(@PathVariable Long id)
    {
        goalService.removeGoal(id);
        return CustomResponse.success(DELETE_SUCCESS);
    }

    @Tag(name = "상위 목표 수정", description = "상위 목표를 수정하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상위 목표 수정 성공"),
    })
    @PatchMapping("/goals/{id}")
    public ResponseEntity<CustomResponse> updateGoal(@RequestBody @Valid GoalUpdateRequest goalSaveRequest)
    {
        return CustomResponse.success(UPDATE_SUCCESS, goalService.updateGoal(goalSaveRequest));
    }

    @Tag(name = "상위 목표 생성", description = "상위 목표를 생성하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상위 목표 생성 성공"),
    })
    @PostMapping("/goals")
    public ResponseEntity<CustomResponse> saveGoal(@RequestBody @Valid GoalSaveRequest goalSaveRequest)
    {
        // 아직 유저 식별 값으로 뭐가 들어올지 몰라 1L로 설정해놨습니다.
        goalService.saveGoal(1L, goalSaveRequest);
        return CustomResponse.success(INSERT_SUCCESS);
    }
}
