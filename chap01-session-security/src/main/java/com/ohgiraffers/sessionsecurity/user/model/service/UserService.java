package com.ohgiraffers.sessionsecurity.user.model.service;

import com.ohgiraffers.sessionsecurity.user.model.dao.UserMapper;
import com.ohgiraffers.sessionsecurity.user.model.dto.LoginUserDTO;
import com.ohgiraffers.sessionsecurity.user.model.dto.SignupDTO;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * - 회원가입, 사용자 정보 조회 등 사용자 중심의 기능을 수행
 * - DAO(UserMapper)와 연동하여 데이터베이스 작업을 처리함
 */
@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 인코더

    @Autowired
    private UserMapper userMapper; // 사용자 정보를 처리하는 MyBatis 매퍼

    /**
     * 사용자 회원가입 기능
     *
     * - 전달받은 사용자 정보를 DB에 저장
     * - 비밀번호는 저장 전에 BCrypt로 암호화
     *
     * @param signupDTO 회원가입 요청 정보를 담은 DTO
     * @return DB 삽입 성공 시 1, 실패 시 0 또는 예외 발생
     */
    public int regist(SignupDTO signupDTO) {

        // 비밀번호를 BCrypt 해시 방식으로 암호화
        signupDTO.setUserPass(passwordEncoder.encode(signupDTO.getUserPass()));

        int result = 0;

        try {
            // 회원가입 요청 정보를 DB에 저장
            result = userMapper.regist(signupDTO);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 (개발 시 참고용)
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 사용자 이름(username)을 기반으로 사용자 정보 조회
     *
     * - 로그인 또는 인증 과정에서 사용됨
     * - 주로 UserDetailsService에서 호출될 수 있음
     *
     * @param username 사용자 이름(ID)
     * @return 사용자 정보 DTO (없을 경우 null 반환)
     */
    public LoginUserDTO findByUsername(String username) {
        LoginUserDTO login = userMapper.findByUsername(username);

        // null 체크 후 결과 반환
        if (!Objects.isNull(login)) {
            return login;
        } else {
            return null;
        }
    }

}
