package com.backend.goal.presentation;

import com.backend.global.common.response.ApiResponse;
import com.backend.goal.application.GoalService;
import com.backend.goal.presentation.dto.GoalSaveRequest;
import com.backend.goal.presentation.dto.GoalUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.global.common.code.SuccessCode.*;

@RestController
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping("/goals/{id}")
    public ResponseEntity<ApiResponse> getGoal(@PathVariable Long id)
    {
        return ApiResponse.success(SELECT_SUCCESS, goalService.getGoal(id));
    }

    @GetMapping("/goals/count")
    public ResponseEntity<ApiResponse> getGoalCount()
    {
        return ApiResponse.success(SELECT_SUCCESS, goalService.getGoalCount());
    }

    @DeleteMapping("/goals/{id}")
    public ResponseEntity<ApiResponse> removeGoal(@PathVariable Long id)
    {
        goalService.removeGoal(id);
        return ApiResponse.success(DELETE_SUCCESS);
    }

    @PatchMapping("/goals/{id}")
    public ResponseEntity<ApiResponse> updateGoal(GoalUpdateRequest goalSaveRequest)
    {
        // 아직 유저 식별 값으로 뭐가 들어올지 몰라 1L로 설정해놨습니다.
        goalService.updateGoal(goalSaveRequest);
        return ApiResponse.success(UPDATE_SUCCESS);
    }

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse> saveGoal(GoalSaveRequest goalSaveRequest)
    {
        // 아직 유저 식별 값으로 뭐가 들어올지 몰라 1L로 설정해놨습니다.
        goalService.saveGoal(1L, goalSaveRequest);
        return ApiResponse.success(INSERT_SUCCESS);
    }
}
