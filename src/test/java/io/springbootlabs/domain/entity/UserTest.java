package io.springbootlabs.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    @DisplayName("User 생성시 User 권한만 세팅")
    public void userCreate() {
        User user = User.createUser();
        assertThat(user.getRoles()).containsOnly(Role.USER);
    }

    @Test
    @DisplayName("Admin 생성시 User 권한만 세팅")
    public void userAdmin() {
        User admin = User.createAdmin();
        assertThat(admin.getRoles()).containsOnly(Role.ADMIN);
    }
}