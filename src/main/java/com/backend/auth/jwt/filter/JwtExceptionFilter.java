package com.backend.auth.jwt.filter;

import com.backend.auth.jwt.exception.BlackListJwtException;
import com.backend.auth.jwt.exception.InvalidJwtException;
import com.backend.auth.jwt.exception.JwtExpiredException;
import com.backend.auth.jwt.exception.NullJwtException;
import com.backend.global.common.code.ErrorCode;
import com.backend.global.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (InvalidJwtException e){
            setErrorResponse(response, e.getErrorCode());
        } catch (JwtExpiredException e){
            setErrorResponse(response, e.getErrorCode());
        } catch (NullJwtException e){
            setErrorResponse(response, e.getErrorCode());
        } catch (BlackListJwtException e){
            setErrorResponse(response, e.getErrorCode());
        }
    }

    // 공통 로직
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json; charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
