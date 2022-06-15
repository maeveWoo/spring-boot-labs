package io.springbootlabs.app.web.config.security;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public class User extends org.springframework.security.core.userdetails.User implements Serializable {
    private String nickName;
    public User(Long id, String password, Collection<? extends GrantedAuthority> authorities, String nickName) {
        super(id.toString(), password, authorities);
        this.nickName = nickName;
    }

    public User(io.springbootlabs.domain.entity.User user) {
        this(user.getId(), user.getPassword(), user.getRoles(), user.getNickName());
    }
}
