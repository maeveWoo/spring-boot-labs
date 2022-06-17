package io.springbootlabs.domain.user;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String password;

    /**
     * 변경가능한 닉네임
     **/
    @Column(length = 20, nullable = false)
    private String nickName;

    /**
     * 최근 업데이트
     **/
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    protected User() {
    }

    private User(List<Role> roles, String password, String nickName) {
        this.lastUpdated = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.roles = roles;
        this.password = password;
        this.nickName = nickName;
    }

    public static User createUser(String password, String nickName) {
        return new User(List.of(Role.USER), password, nickName);
    }

    public static User createAdmin(String password, String nickName) {
        return new User(List.of(Role.ADMIN), password, nickName);
    }

    private void addRoles(Role role) {
        // TODO 슈퍼 관리자만 사용할 수 있게.
        roles.add(role);
    }
}
