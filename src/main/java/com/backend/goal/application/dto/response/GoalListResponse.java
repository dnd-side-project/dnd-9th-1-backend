package com.backend.goal.application.dto.response;

import com.backend.goal.domain.GoalListResponseDto;

import java.time.LocalDate;

public record GoalListResponse(Long goalId, String title, LocalDate startDate, LocalDate endDate, Integer entireDetailGoalCnt, Integer completedDetailGoalCnt) {

   public static GoalListResponse from(GoalListResponseDto goalListResponseDto)
   {
       return new GoalListResponse(goalListResponseDto.goalId(),goalListResponseDto.title(),goalListResponseDto.startDate(),goalListResponseDto.endDate(),goalListResponseDto.entireDetailGoalCnt(),goalListResponseDto.completedDetailGoalCnt());
   }

}
