package com.backend.plan.presentation;

import com.backend.global.common.code.SuccessCode;
import com.backend.global.common.response.ApiResponse;
import com.backend.plan.application.PlanService;
import com.backend.plan.presentation.dto.PlanSaveRequest;
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
    public ResponseEntity<ApiResponse> getPlan(Long id)
    {
        return ApiResponse.success(SELECT_SUCCESS, planService.getPlan(id));
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<ApiResponse> removePlan(Long id)
    {
        planService.removePlan(id);
        return ApiResponse.success(DELETE_SUCCESS);
    }

}
