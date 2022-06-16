package io.springbootlabs.app.web.config.security;

import io.springbootlabs.domain.user.User;
import io.springbootlabs.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(Long.parseLong(id));
        if(user.isPresent()) {
            return new io.springbootlabs.app.web.config.security.User(user.get());
        } else {
            throw new UsernameNotFoundException("{id} not found");
        }
    }
}
