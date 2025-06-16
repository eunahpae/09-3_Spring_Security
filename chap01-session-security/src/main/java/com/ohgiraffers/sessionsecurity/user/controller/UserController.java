package com.ohgiraffers.sessionsecurity.user.controller;

import com.ohgiraffers.sessionsecurity.user.model.dto.SignupDTO;
import com.ohgiraffers.sessionsecurity.user.model.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 사용자 관련 요청을 처리하는 컨트롤러 클래스
 *
 * - 회원가입 화면 요청 및 가입 처리 로직을 담당함
 * - "/user" 경로를 기본 매핑으로 사용
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * 생성자를 통한 의존성 주입
     *
     * @param userService 사용자 관련 비즈니스 로직을 처리하는 서비스
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 화면 요청 처리
     *
     * - GET 방식으로 "/user/signup" 요청 시 회원가입 화면 반환
     * - 반환형이 void일 경우 요청 경로에 해당하는 뷰 이름을 자동 매핑 (예: "user/signup.html")
     */
    @GetMapping("/signup")
    public void signup() {
    }

    /**
     * 회원가입 요청 처리
     *
     * - POST 방식으로 "/user/signup" 요청이 들어오면 회원가입 로직 실행
     * - 가입 성공 여부에 따라 적절한 뷰와 메시지를 설정
     *
     * @param mv         ModelAndView 객체 (뷰 이름과 데이터를 함께 반환)
     * @param signupDTO  회원가입 폼으로부터 전달된 사용자 정보
     * @return           처리 결과와 메시지를 담은 ModelAndView 객체
     */
    @PostMapping("/signup")
    public ModelAndView signup(ModelAndView mv, @ModelAttribute SignupDTO signupDTO) {
        // 회원가입 처리 서비스 호출
        int result = userService.regist(signupDTO);
        String message = "";

        // 가입 성공 여부에 따라 메시지 및 이동할 뷰 설정
        if (result > 0) {
            message = "회원가입이 정상적으로 완료되었습니다.";
            mv.setViewName("auth/login"); // 로그인 화면으로 이동
        } else {
            message = "회원가입에 실패하였습니다.";
            mv.setViewName("user/signup"); // 다시 회원가입 화면으로 이동
        }

        // 메시지를 모델에 추가
        mv.addObject("message", message);

        return mv;
    }

}
