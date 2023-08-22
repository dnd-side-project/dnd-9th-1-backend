package com.backend.retrospect.presentation.dto.request;

import com.backend.retrospect.domain.Guide;
import com.backend.retrospect.domain.SuccessLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record RetrospectSaveRequest(

        @JsonProperty("has_guide")
        @Schema(description = "가이드 질문 유무", example = "true")
        @NotNull(message = "가이드 질문의 유무를 선택해주세요.")
        Boolean hasGuide,

        @JsonProperty("contents")
        @Schema(description = "회고 글", 
                example =  "{\"LIKED\": \"단기간 집중을 했던 점이 좋았어요.\", \"LACKED\": \"아쉬웠던 점은 없었어요.\", \"LEARNED\": \"하나에 집중해야 원하는 것을 빠르게 얻을 수 있다는 것을 배웠어요.\", \"LONGED_FOR\": \"돈을 많이 벌고자 하였어요.\"}",
                implementation = Map.class)
        @NotNull(message = "회고글의 내용은 빈칸일 수 없습니다.")
        Map<Guide, String> contents,

        @JsonProperty("success_level")
        @Schema(description = "마음 채움도", example = "LEVEL3")
        @NotNull(message = "마음 채움도를 선택해주세요.")
        SuccessLevel successLevel
) {
}
