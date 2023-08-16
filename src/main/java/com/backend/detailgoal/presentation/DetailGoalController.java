package com.backend.detailgoal.presentation;

import com.backend.detailgoal.application.DetailGoalService;
import com.backend.detailgoal.presentation.dto.request.DetailGoalSaveRequest;
import com.backend.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.backend.global.common.code.SuccessCode.INSERT_SUCCESS;
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

    @Operation(summary = "하위 목표 상세 조회", description = "하위 목표를 상세 조회하는 API 입니다.")
    @GetMapping("/detail-goals/{id}")
    public ResponseEntity<CustomResponse> getDetailGoal(@PathVariable Long id)
    {
        return CustomResponse.success(SELECT_SUCCESS, detailGoalService.getDetailGoal(id));
    }

    @Operation(summary = "하위 목표 리스트 생성", description = "하위 목표를 생성하는 API 입니다.")
    @PostMapping("/goals/{id}/detail-goals")
    public ResponseEntity<CustomResponse> saveDetailGoal(@PathVariable Long id, @RequestBody @Valid DetailGoalSaveRequest detailGoalSaveRequest)
    {
        detailGoalService.saveDetailGoal(id, detailGoalSaveRequest);
        return CustomResponse.success(INSERT_SUCCESS);
    }
}
