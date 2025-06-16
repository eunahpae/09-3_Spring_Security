package com.ohgiraffers.sessionsecurity.user.model.dto;

/**
 * 이 클래스는 java.io.Serializable 인터페이스를 구현하여 직렬화를 지원한다.
 *
 * comment. 직렬화란 ?
 * 자바 객체를 바이트 형태로 변환하여 파일에 저장하거나
 * 네트워크를 통해 전송할 수 있도록 만드는 기능이다.
 * 역직렬화를 통해 다시 객체 형태로 복원할 수 있다.
 */
public class SignupDTO implements java.io.Serializable {

    /* serialVersionUID는 클래스의 직렬화 버전을 명시하는 식별자이다.
     * 명시하지 않으면 JVM이 자동으로 생성하지만, 클래스 구조가 변경될 경우
     * 호환성 문제가 발생할 수 있으므로 명시적으로 선언하는 것이 좋다.*/
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userName;
    private String userPass;
    private String role;

    public SignupDTO() {
    }

    public SignupDTO(String userId, String userName, String userPass, String role) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SignupDTO{" +
            "userId='" + userId + '\'' +
            ", userName='" + userName + '\'' +
            ", userPass='" + userPass + '\'' +
            ", role='" + role + '\'' +
            '}';
    }
}
