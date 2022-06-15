package io.springbootlabs.domain.entity;

public enum Role {
    USER("사용자"),
    ADMIN("조회용 관리자"),
    WRITE_ADMIN("관리자"),
    SUPER_ADMIN("슈퍼 관리자")
    ;

    private String description;

    Role(String description) {
        this.description = description;
    }
}
