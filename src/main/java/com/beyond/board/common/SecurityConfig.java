package com.beyond.board.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // PreAuthorized 를 미리 다 가져온다. (컨트롤러 딴에서 처리하는게 아님)
public class SecurityConfig {
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeRequests().antMatchers("/", "/author/create", "/author/login").permitAll().anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/author/login")
                    // 사전에 구현되어 있는 doLogin 메서드 그대로 사용
                    .loginProcessingUrl("/doLogin")
                    .usernameParameter("email")
                    .successHandler(loginSuccessHandler)
                .passwordParameter("password")
                .and()
                .logout().logoutUrl("/doLogout")
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // 객체만들고 AuthorService에 주입
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
