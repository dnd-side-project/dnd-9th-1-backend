package com.backend.global.common.response;

import com.backend.global.common.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {

    private int code;
    private String message;
    private T data;


    public CustomResponse(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseEntity<CustomResponse<T>> success(final SuccessCode successCode, final T data) {
        return ResponseEntity.status(successCode.getStatus())
                .body(new CustomResponse<>(successCode.getStatus(), successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<CustomResponse<T>> success(final SuccessCode successCode) {
        return ResponseEntity.status(successCode.getStatus())
                .body(new CustomResponse<>(successCode.getStatus(), successCode.getMessage(), null));
    }
}