package com.toy.tripstory.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.tripstory.common.BaseResponse;
import com.toy.tripstory.error.ErrorType;
import com.toy.tripstory.error.exception.BadRequestException;
import com.toy.tripstory.error.exception.ForbiddenException;
import com.toy.tripstory.error.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    // JSON 변환을 위해 ObjectMapper 사용
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JWT 인증 필터에서 발생하는 예외를 처리합니다.
     * 배드 리퀘스트 예외(BadRequestException)와 일반 예외를 구분하여 처리하며,
     * 각각의 예외에 따라 적절한 에러 응답을 클라이언트에 반환합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException 서블릿 관련 예외
     * @throws IOException      입출력 관련 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 응답 인코딩 설정
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            // 필터 체인 실행: 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            // 인증 인가 예외 발생 시 처리
            // 인증 인가 예외에 해당하는 에러 타입을 포함한 에러 응답을 JSON 형태로 반환
            ErrorType errorType = e.getErrorType();
            String errorResponse = objectMapper.writeValueAsString(BaseResponse.fail(errorType));
            response.getWriter().write(errorResponse);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.error("UnauthroizedException 발생: {}", e.getMessage(), e);
        } catch (ForbiddenException e) {
            // 접근 권한에 대한 예외 발생 시 처리
            // 접근 권한 예외에 해당하는 에러 타입을 포함한 에러 응답을 JSON 형태로 반환
            String errorResponse = objectMapper.writeValueAsString(BaseResponse.fail(e.getErrorType()));
            response.getWriter().write(errorResponse);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            log.error("ForbiddenException 발생: {}", e.getMessage(), e);
        } catch (BadRequestException e) {
            // 비즈니스 예외 발생 시 처리
            // 비즈니스 예외에 해당하는 에러 타입을 포함한 에러 응답을 JSON 형태로 반환
            String errorResponse = objectMapper.writeValueAsString(BaseResponse.fail(e.getErrorType()));
            response.getWriter().write(errorResponse);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            log.error("BadRequestException 발생: {}", e.getMessage(), e);
        } catch (Exception e) {
            // 일반 예외 발생 시 처리
//             서버 에러에 해당하는 에러 타입을 포함한 에러 응답을 JSON 형태로 반환
            ErrorType errorType = ErrorType.SERVER_ERROR;

            String errorResponse = objectMapper.writeValueAsString(BaseResponse.fail(errorType, e.getCause().toString(), e.getMessage()));
            response.getWriter().write(errorResponse);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Exception 발생: {}", e.getMessage(), e);
        }

    }
}
