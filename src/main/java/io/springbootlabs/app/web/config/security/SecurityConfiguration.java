package io.springbootlabs.app.web.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(auth ->
                        {
                            try {
                                auth
                                        .antMatchers("/", "/login", "/actuator/health", "/test/**")
                                        .permitAll()
                                        .antMatchers("/favicon.ico", "/js/**", "/css/**", "/font-awesome/**", "/img/**", "/fonts/**", "/vendor/**", "/error")
                                        .permitAll()
                                        .antMatchers("/admin/**").hasAnyAuthority("ADMIN", "WRITE_ADMIN", "SUPER_ADMIN")
                                        .anyRequest().authenticated()
                                        .and()
                                        .formLogin()
                                        .loginPage("/login")
                                        .defaultSuccessUrl("/")
                                        .and()
                                        .exceptionHandling().accessDeniedPage("/error-forbidden");
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .csrf().disable() // CSRF 읽고 정리
                .httpBasic(withDefaults());
        return http.build();
    }
}
