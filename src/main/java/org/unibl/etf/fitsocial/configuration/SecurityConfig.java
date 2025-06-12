package org.unibl.etf.fitsocial.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       /* http.authorizeHttpRequests(authz -> authz.requestMatchers("/api/**", "/login", "/register").permitAll() // javne rute
                .anyRequest().authenticated() // sve ostalo zahtijeva autentikaciju
        );

        */
        http
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                );

        return http.build();


    }
}
