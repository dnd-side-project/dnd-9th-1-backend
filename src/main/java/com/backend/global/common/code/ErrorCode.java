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

    /* Plan */
    PLAN_NOT_FOUND(NOT_FOUND.value(), "PLAN-001", "상위 목표가 존재하지 않습니다."),

    /* Auth */
    TOKEN_EXPIRED(UNAUTHORIZED.value(), "AUTH-001", "토큰의 유효기간이 만료되었습니다."),

    INVALID_TOKEN(BAD_REQUEST.value(), "AUTH-002", "잘못된 형식의 토큰이 입력되었습니다."),

    MALFORMED_TOKEN(BAD_REQUEST.value(), "AUTH-003", "잘못된 구조의 토큰이 입력되었습니다."),

    TOKEN_ERROR(BAD_REQUEST.value(), "AUTH-004" ,"토큰 검증 시 알 수 없는 오류가 발생했습니다." );

    private final int status;

    private final String code;

    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
