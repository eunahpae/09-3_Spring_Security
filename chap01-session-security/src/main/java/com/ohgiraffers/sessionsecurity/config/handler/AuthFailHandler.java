package com.ohgiraffers.sessionsecurity.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 로그인 실패 시 처리하는 핸들러 클래스
 * <p>
 * - Spring Security의 SimpleUrlAuthenticationFailureHandler를 상속하여 실패 처리를 커스터마이징 - 로그인 실패 원인에 따라
 * 사용자에게 적절한 오류 메시지를 전달함
 */
@Configuration
public class AuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * 인증 실패 시 실행되는 메서드
     *
     * @param request   클라이언트 요청 객체
     * @param response  클라이언트 응답 객체
     * @param exception 발생한 인증 예외
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "";

        // 아이디 또는 비밀번호가 틀린 경우
        if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.";
        }
        // 인증 서비스 내부 오류 발생 시
        else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "서버에서 오류가 발생하였습니다. 관리자에게 문의해주세요";
        }
        // 사용자 ID를 찾을 수 없는 경우
        else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "존재하지 않는 ID입니다.";
        }
        // 인증 자격이 누락된 경우 (예: SecurityContext에 인증 정보가 없음)
        else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증요청이 거부되었습니다.";
        }
        // 그 외 정의되지 않은 예외 처리
        else {
            errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다.";
        }

        // 메시지를 URL 인코딩하여 쿼리스트링에 포함 (한글 깨짐 방지)
        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");

        // 로그인 실패 후 이동할 URL 지정 (/auth/fail?message=...)
        setDefaultFailureUrl("/auth/fail?message=" + errorMessage);

        // 부모 클래스(SimpleUrlAuthenticationFailureHandler)의 처리 로직 수행
        // → 지정된 실패 URL로 리다이렉트 처리 포함
        super.onAuthenticationFailure(request, response, exception);
    }
}
