package com.example.authCrud.config;

import com.example.authCrud.security.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService uds;

    public SecurityConfig(CustomUserDetailsService uds) {
        this.uds = uds;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(a -> a
            .requestMatchers("/register", "/login", "/css/**").permitAll()
            .anyRequest().authenticated()
          )
          .formLogin(f -> f
            .loginPage("/login")
            .defaultSuccessUrl("/greeting", true)
            .failureUrl("/login?error=true")
            .permitAll()
          )
          .logout(l -> l
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll()
          )
          .userDetailsService(uds);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
