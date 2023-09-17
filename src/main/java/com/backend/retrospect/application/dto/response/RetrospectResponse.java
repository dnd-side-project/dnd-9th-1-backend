package com.backend.retrospect.application.dto.response;

import com.backend.retrospect.domain.Guide;
import com.backend.retrospect.domain.RetrospectContent;
import com.backend.retrospect.domain.SuccessLevel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RetrospectResponse(
        @Schema(description = "가이드 질문의 유무", example = "true")
        Boolean hasGuide,

        @Schema(description = "가이드 질문과 사용자의 회고 글", example = "{\"LIKED\": \"단기간 집중을 했던 점이 좋았어요.\", \"LACKED\": \"아쉬웠던 점은 없었어요.\", \"LEARNED\": \"하나에 집중해야 원하는 것을 빠르게 얻을 수 있다는 것을 배웠어요.\", \"LONGED_FOR\": \"돈을 많이 벌고자 하였어요.\"}")
        Map<Guide, String> contents,

        @Schema(description = "마음 채움도", example = "LEVEL3")
        SuccessLevel successLevel

) {
    public static RetrospectResponse from(Boolean hasGuide, List<RetrospectContent> contents, SuccessLevel successLevel) {
        Map<Guide, String> retrospectContents = new HashMap<>();
        for(RetrospectContent content : contents){
            retrospectContents.put(content.getGuide(), content.getContent());
        }
        return new RetrospectResponse(hasGuide, retrospectContents, successLevel);
    }
}
