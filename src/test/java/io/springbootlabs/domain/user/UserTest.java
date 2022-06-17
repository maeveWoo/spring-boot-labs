package io.springbootlabs.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    @DisplayName("User 생성시 User 권한만 세팅")
    public void userCreate() {
        User user = User.createUser("test", "test");

        assertThat(user.getRoles()).containsOnly(Role.USER);
        assertThat(user.getNickName()).isNotNull();
        assertThat(user.getPassword()).isNotNull();
    }

    @Test
    @DisplayName("Admin 생성시 User 권한만 세팅")
    public void userAdmin() {
        User admin = User.createAdmin("test", "test");

        assertThat(admin.getRoles()).containsOnly(Role.ADMIN);
        assertThat(admin.getNickName()).isNotNull();
        assertThat(admin.getPassword()).isNotNull();
    }
}