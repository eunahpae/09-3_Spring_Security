package com.ohgiraffers.sessionsecurity.auth.model.service;

import com.ohgiraffers.sessionsecurity.auth.model.AuthDetails;
import com.ohgiraffers.sessionsecurity.user.model.dto.LoginUserDTO;
import com.ohgiraffers.sessionsecurity.user.model.service.UserService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증을 위한 서비스 클래스
 *
 * - 스프링 시큐리티에서 제공하는 UserDetailsService 인터페이스를 구현하여,
 *   사용자 정보를 데이터베이스 등에서 로딩하는 책임을 가진다.
 * - 인증(Authentication) 과정에서 username(일반적으로 ID 또는 이메일)을 기준으로 사용자 정보를 조회하고,
 *   이를 UserDetails 타입으로 반환해야 한다.
 */
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * 사용자 이름(username)을 기반으로 사용자 정보를 로드하는 메서드
     *
     * - 스프링 시큐리티의 인증 매커니즘에서 사용되며, 로그인 시 입력된 username을 기반으로 사용자 정보를 조회한다.
     * - 반환된 UserDetails 객체는 스프링 시큐리티가 비밀번호 비교 및 권한 확인 등에 사용한다.
     *
     * @param username 로그인 시 입력된 사용자 이름 (ID 또는 이메일 등)
     * @return UserDetails 사용자 정보 객체 (username, password, roles 포함)
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginUserDTO login = userService.findByUsername(username);

        if (Objects.isNull(login)) {
            throw new UsernameNotFoundException("해당하는 회원 정보가 존재하지 않습니다.");
        }

        return new AuthDetails(login);
    }
}
