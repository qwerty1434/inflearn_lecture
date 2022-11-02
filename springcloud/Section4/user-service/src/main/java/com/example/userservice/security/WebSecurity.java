//package com.example.userservice.security;
//
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//
//@EnableWebSecurity
//public class WebSecurity {
//
//    @Bean
//    protected WebSecurityCustomizer configure(HttpSecurity http) throws Exception{
//        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
//        http.headers().frameOptions().disable();
//        return null; // void타입이 안되서 WebSecurityCustomizer 적어뒀는데 저거말고 딴거 반환해야 되는거 같음
//    }
//
//
//}