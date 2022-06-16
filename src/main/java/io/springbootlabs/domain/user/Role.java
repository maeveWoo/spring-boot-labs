package io.springbootlabs.domain.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER("사용자"),
    ADMIN("조회용 관리자"),
    WRITE_ADMIN("관리자"),
    SUPER_ADMIN("슈퍼 관리자")
    ;

    private String description;

    Role(String description) {
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
