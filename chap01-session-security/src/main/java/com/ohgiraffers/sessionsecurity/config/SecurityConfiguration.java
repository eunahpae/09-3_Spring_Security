package com.ohgiraffers.sessionsecurity.config;

import com.ohgiraffers.sessionsecurity.common.UserRole;
import com.ohgiraffers.sessionsecurity.config.handler.AuthFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 스프링 시큐리티 보안 설정 클래스
 * - 이 클래스는 보안 관련 Bean을 정의하며, 웹 보안 설정을 활성화한다.
 */
@Configuration                      // 이 클래스를 스프링 설정 클래스로 지정 (빈 등록 가능)
@EnableWebSecurity                 // Spring Security의 웹 보안 기능을 활성화
public class SecurityConfiguration {

    @Autowired
    private AuthFailHandler authFailHandler;  // 로그인 실패 시 커스텀 핸들러 주입

    /**
     * PasswordEncoder 빈 등록
     * - BCryptPasswordEncoder는 스프링 시큐리티에서 제공하는 비밀번호 해시 함수 구현체
     * - 사용자의 비밀번호를 암호화하여 저장하고, 인증 시 암호화된 값과 비교
     *
     * @return 암호화에 사용될 PasswordEncoder 구현체 (BCrypt 방식)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * WebSecurityCustomizer 빈 등록
     * - 시큐리티 필터를 거치지 않고 접근을 허용할 리소스를 지정
     * - 정적 자원(css, js, 이미지 등)에 대해 보안 필터를 적용하지 않도록 설정
     *
     * @return WebSecurityCustomizer 구현체
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        /* 요청에 대한 권한 체크 설정 */
        http.authorizeHttpRequests(auth -> {
            // 인증 없이 전체 접근 허용할 URL (로그인/회원가입/메인 페이지 등)
            auth.requestMatchers("/auth/login", "/user/signup", "/auth/fail", "/", "/main").permitAll();
            // ADMIN 권한이 있어야 접근 가능한 URL
            auth.requestMatchers("/admin/*").hasAnyAuthority(UserRole.ADMIN.getRole());
            // USER 권한이 있어야 접근 가능한 URL
            auth.requestMatchers("/user/*").hasAnyAuthority(UserRole.USER.getRole());
            // 위에서 명시한 URL 외의 나머지 모든 요청은 인증 필요
            auth.anyRequest().authenticated();

            /* 로그인 설정 */
        }).formLogin(login -> {
            // 사용자 정의 로그인 페이지 경로
            login.loginPage("/auth/login");
            // 로그인 폼에서 사용자명을 받을 파라미터명 설정
            login.usernameParameter("user");
            // 로그인 폼에서 비밀번호를 받을 파라미터명 설정
            login.passwordParameter("pass");
            // 로그인 성공 시 이동할 기본 URL (항상 / 로 이동)
            login.defaultSuccessUrl("/", true);
            // 로그인 실패 시 호출될 커스텀 실패 핸들러
            login.failureHandler(authFailHandler);

            /* 로그아웃 설정 */
        }).logout(logout -> {
            // 로그아웃 요청을 처리할 URL 지정
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
            // 로그아웃 시 쿠키 삭제
            logout.deleteCookies("JSESSIONID");
            // 로그아웃 시 세션 무효화
            logout.invalidateHttpSession(true);
            // 로그아웃 성공 후 이동할 URL
            logout.logoutSuccessUrl("/");

            /* 세션 관리 설정 */
        }).sessionManagement(session -> {
            // 동시에 허용되는 세션 수 제한 (1개로 제한)
            session.maximumSessions(1);
            // 세션이 유효하지 않을 때 이동할 URL
            session.invalidSessionUrl("/");

            /* CSRF 설정 (Cross-Site Request Forgery 보호 비활성화)
             *
             * CSRF란?
             * - '크로스 사이트 요청 위조(Cross-Site Request Forgery)'의 약자로,
             *   사용자가 자신의 의지와 무관하게 공격자가 의도한 요청을 하게 만드는 공격 방식이다.
             * - 예를 들어, 로그인한 사용자가 악의적인 사이트를 방문했을 때, 사용자의 쿠키 정보를 이용해
             *   실제 사이트에 의도치 않은 요청(예: 게시글 등록, 회원탈퇴 등)이 전송될 수 있다.
             *
             * Spring Security에서의 CSRF 보호:
             * - 기본적으로 POST, PUT, DELETE 같은 변경 요청에 대해 CSRF 토큰을 요구함으로써 공격을 방지한다.
             * - 하지만 REST API 서버나 테스트 환경에서는 불편을 줄이기 위해 비활성화하는 경우가 있다.
             */
        }).csrf(csrf -> csrf.disable()); // CSRF 보호 비활성화 (API 등 사용 시 편의상 끄기도 함, 개발단계에서만 꺼둘 것)

        return http.build(); // SecurityFilterChain 반환
    }

}
