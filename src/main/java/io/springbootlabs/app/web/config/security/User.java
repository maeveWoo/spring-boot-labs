package io.springbootlabs.app.web.config.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Getter
public class User extends org.springframework.security.core.userdetails.User implements Serializable {
    private String nickName;
    public User(Long id, String password, Collection<? extends GrantedAuthority> authorities, String nickName) {
        super(id.toString(), password, authorities);
        this.nickName = nickName;
    }

    public User(io.springbootlabs.domain.user.User user) {
        this(user.getId(), user.getPassword(), user.getRoles(), user.getNickName());
    }
}
