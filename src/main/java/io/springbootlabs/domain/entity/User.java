package io.springbootlabs.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String nickName;

    /** 최근 업데이 **/
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    protected User() {}

    private User(List<Role> roles) {
        this.lastUpdated = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.roles = roles;
    }

    static User createUser() {
        return new User(List.of(Role.USER));
    }

    static User createAdmin() {
        return new User(List.of(Role.ADMIN));
    }

    private void addRoles(Role role) {
        // TODO 슈퍼 관리자만 사용할 수 있게.
        roles.add(role);
    }
}
