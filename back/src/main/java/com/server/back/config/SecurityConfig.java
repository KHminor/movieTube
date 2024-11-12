package com.server.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // Swagger UI 접근 허용
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v2/api-docs", "/v2/api-docs/**").permitAll()
                // "/user/**" 패턴에 대해서는 인증이 필요
                .antMatchers("/user/**").authenticated()
                // 나머지 요청에 대해서는 접근 허용
                .anyRequest().permitAll()
                .and()
                .csrf().disable(); // CSRF 보호 비활성화 (Swagger UI를 사용할 때 편의를 위해)
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
