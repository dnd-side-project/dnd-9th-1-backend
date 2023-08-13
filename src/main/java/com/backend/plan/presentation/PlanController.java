package com.backend.plan.presentation;

import com.backend.global.common.code.SuccessCode;
import com.backend.global.common.response.ApiResponse;
import com.backend.plan.application.PlanService;
import com.backend.plan.presentation.dto.PlanSaveRequest;
import com.backend.plan.presentation.dto.PlanUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.global.common.code.SuccessCode.*;

@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping("/plans/{id}")
    public ResponseEntity<ApiResponse> getPlan(@PathVariable Long id)
    {
        return ApiResponse.success(SELECT_SUCCESS, planService.getPlan(id));
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<ApiResponse> removePlan(@PathVariable Long id)
    {
        planService.removePlan(id);
        return ApiResponse.success(DELETE_SUCCESS);
    }

    @PatchMapping("/plans/{id}")
    public ResponseEntity<ApiResponse> updatePlan(PlanUpdateRequest planSaveRequest)
    {
        // 아직 유저 식별 값으로 뭐가 들어올지 몰라 1L로 설정해놨습니다.
        planService.updatePlan(planSaveRequest);
        return ApiResponse.success(UPDATE_SUCCESS);
    }

    @PostMapping("/plans")
    public ResponseEntity<ApiResponse> savePlan(PlanSaveRequest planSaveRequest)
    {
        // 아직 유저 식별 값으로 뭐가 들어올지 몰라 1L로 설정해놨습니다.
        planService.savePlan(1L, planSaveRequest);
        return ApiResponse.success(INSERT_SUCCESS);
    }
}
