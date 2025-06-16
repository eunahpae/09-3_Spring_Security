package com.ohgiraffers.sessionsecurity.auth.model;

import com.ohgiraffers.sessionsecurity.user.model.dto.LoginUserDTO;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security 에서 사용자 인증 정보를 담기 위한 클래스
 *
 * - UserDetails 인터페이스를 구현하여, Spring Security가 로그인한 사용자 정보를 다룰 수 있게 한다.
 * - 내부적으로 LoginUserDTO를 포함하고 있으며, 해당 DTO를 통해 사용자 정보를 가져온다.
 */
public class AuthDetails implements UserDetails {

    // 로그인한 사용자 정보를 담고 있는 DTO
    private LoginUserDTO loginUserDTO;

    // 기본 생성자
    public AuthDetails() {
    }

    // LoginUserDTO를 기반으로 AuthDetails를 생성하는 생성자
    public AuthDetails(LoginUserDTO loginUserDTO) {
        this.loginUserDTO = loginUserDTO;
    }

    // 내부 DTO를 반환하는 getter
    public LoginUserDTO getLoginUserDTO() {
        return loginUserDTO;
    }

    // 내부 DTO를 설정하는 setter
    public void setLoginUserDTO(LoginUserDTO loginUserDTO) {
        this.loginUserDTO = loginUserDTO;
    }

    /**
     * 사용자의 권한(역할)을 반환
     *
     * - Spring Security의 인가(Authorization) 기능에서 사용됨
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        loginUserDTO.getRole().forEach(role -> authorities.add(() -> role));
        return authorities;
    }

    /**
     * 사용자의 비밀번호를 반환
     *
     * - Spring Security가 로그인 시 입력한 비밀번호와 비교할 때 사용
     */
    @Override
    public String getPassword() {
        return loginUserDTO.getPassword();
    }

    /**
     * 사용자의 이름(또는 ID)을 반환
     *
     * - Spring Security가 인증 시 사용자 식별을 위해 사용
     */
    @Override
    public String getUsername() {
        return loginUserDTO.getUserName();
    }

    /**
     * 계정이 만료되지 않았는지 여부
     *
     * - true를 반환하면 만료되지 않은 상태로 간주
     * - 보통 유효기간 검증 로직이 필요한 경우 오버라이드하여 구현
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠기지 않았는지 여부
     *
     * - true를 반환하면 잠기지 않은 상태로 간주
     * - 관리자에 의해 잠금된 계정 등을 처리할 때 사용
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호가 만료되지 않았는지 여부
     *
     * - true를 반환하면 비밀번호가 아직 유효한 상태로 간주
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되어 있는지 여부
     *
     * - true를 반환하면 활성 사용자로 간주
     * - 탈퇴한 사용자나 정지된 사용자를 필터링할 때 사용
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
