package com.capstone.storyforest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        // jwt는 세션을 stateless로 관리하기 때문에  방어하지 않아도 되서
        http
                .csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        // login root join 에 대해서는 모든 요청을 허용함.
                        .requestMatchers("/login", "/", "/join").permitAll()
                        // admin 경로는 ADMIN 만 접근할 수 있음
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 그 외 요청은 로그인한 사용자만 접근할 수 있도록 함.
                        .anyRequest().authenticated());

        //세션 설정
        // jwt에서는 세션을 stateless 상태로 관리함.
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));



        return http.build();
    }
}
