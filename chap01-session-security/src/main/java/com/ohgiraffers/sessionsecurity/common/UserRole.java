package com.ohgiraffers.sessionsecurity.common;

/**
 * 사용자 역할(Role)을 정의하는 열거형(enum) 클래스
 * <p>
 * - 열거형(enum)은 고정된 상수 집합을 정의할 때 사용되며, 자바에서 타입 안정성을 보장한다. - 이 클래스는 스프링 시큐리티 등에서 권한(Role)을 부여할 때
 * 사용된다. - 예: 사용자는 USER 역할, 관리자는 ADMIN 역할을 가질 수 있음
 */
public enum UserRole {

    // 일반 사용자 역할
    USER("USER"),

    // 관리자 역할
    ADMIN("ADMIN");

    // 각 enum 상수에 매핑되는 문자열 값 (예: "USER", "ADMIN")
    private final String role;

    /**
     * 생성자 - enum 생성자는 private이 기본이며, 각 enum 상수 선언 시 호출된다.
     *
     * @param role 문자열 형태의 역할 이름
     */
    UserRole(String role) {
        this.role = role;
    }

    /**
     * 역할 이름 반환 메서드 - 외부에서 enum의 문자열 값을 조회할 때 사용
     *
     * @return 역할 이름 문자열 ("USER" 또는 "ADMIN")
     */
    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
            "role='" + role + '\'' +
            '}';
    }
}
