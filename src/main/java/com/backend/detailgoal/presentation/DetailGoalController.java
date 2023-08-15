package com.backend.detailgoal.presentation;

import com.backend.detailgoal.application.DetailGoalService;
import com.backend.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.backend.global.common.code.SuccessCode.SELECT_SUCCESS;

@RestController
@RequiredArgsConstructor
@Tag(name = "detailGoal", description = "하위 목표 API")
public class DetailGoalController {

    private final DetailGoalService detailGoalService;

    @Operation(summary = "하위 목표 리스트 조회", description = "하위 목표 리스트를 조회하는 API 입니다.")
    @GetMapping("/goals/{id}/detail-goals")
    public ResponseEntity<CustomResponse> getDetailGoalList(@PathVariable Long id)
    {
        return CustomResponse.success(SELECT_SUCCESS, detailGoalService.getDetailGoalList(id));
    }
}
