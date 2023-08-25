package com.backend.global.common.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {

    /*
    Global Error
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "COMMON-001", "서버에서 처리할 수 없습니다."),

    INVALID_INPUT_VALUE(BAD_REQUEST.value(), "COMMON-002", "유효성 검증에 실패했습니다."),

    BINDING_ERROR(BAD_REQUEST.value(), "COMMON-003", "요청 값 바인딩에 실패했습니다."),

    BAD_REQUEST_ERROR(BAD_REQUEST.value(), "COMMON-004", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(BAD_REQUEST.value(), "COMMON-005", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(BAD_REQUEST.value(), "COMMON-006", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(BAD_REQUEST.value(), "COMMON-007", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(BAD_REQUEST.value(), "COMMON-008", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(BAD_REQUEST.value(), "COMMON-009", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(BAD_REQUEST.value(), "COMMON-010", "com.fasterxml.jackson.core Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(NOT_FOUND.value(), "COMMON-011", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(NOT_FOUND.value(), "COMMON-012", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(NOT_FOUND.value(), "COMMON-013", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(NOT_FOUND.value(), "COMMON-014", "Header에 데이터가 존재하지 않는 경우 "),

    /*
    Business Error
     */


    /* Goal */
    GOAL_NOT_FOUND(NOT_FOUND.value(), "GOAL-001", "상위 목표가 존재하지 않습니다."),

    ENTIRE_DETAIL_GOAL_CNT_INVALID(BAD_REQUEST.value(), "GOAL-002", "총 하위 목표 개수가 0개일때는 뺄수 없습니다."),

    COMPLETED_DETAIL_GOAL_CNT_INVALID(BAD_REQUEST.value(), "GOAL-003", "성공한 하위목표 개수가 0개 일때는 뺄 수 없습니다."),

    RECOVER_GOAL_IMPOSSIBLE(BAD_REQUEST.value(), "GOAL-004", "상위 목표가 보관함에 있을때 채움함으로 복구할 수 있습니다."),

    /* Detail Goal */
    DETAIL_GOAL_NOT_FOUND(NOT_FOUND.value(), "DETAIL-GOAL-001", "하위 목표가 존재하지 않습니다."),


    /* Auth */
    TOKEN_EXPIRED(UNAUTHORIZED.value(), "AUTH-001", "토큰의 유효기간이 만료되었습니다."),

    INVALID_TOKEN(BAD_REQUEST.value(), "AUTH-002", "잘못된 형식의 토큰 입력입니다."),

    MEMBER_NOT_FOUND(NOT_FOUND.value(), "AUTH-003", "회원이 존재하지 않습니다."),

    NO_TOKEN_PROVIDED(UNAUTHORIZED.value(), "AUTH-004", "토큰이 입력되지 않았습니다."),

    BLACK_LIST_TOKEN(UNAUTHORIZED.value(), "AUTH-005", "로그아웃하거나 회원탈퇴한 사용자입니다."),

    /* Retrospect */
    RETROSPECT_IS_NOT_WRITTEN(NOT_FOUND.value(), "RETROSPECT-001", "회고가 작성되지 않은 상위 목표입니다."),

    ALREADY_HAS_RETROSPECT(BAD_REQUEST.value(), "RETROSPECT-002", "이미 회고를 작성한 상위목표입니다."),

    CONTENT_TOO_LONG(BAD_REQUEST.value(), "RETROSPECT-003", "회고글의 길이가 제한된 길이를 초과하였습니다.");

    private final int status;

    private final String code;

    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
