package com.example.jwttutorial.service;

import java.util.Collections;
import java.util.Optional;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.User;
import com.example.jwttutorial.repository.UserRepository;
import com.example.jwttutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 로직 수행
    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) { // 이미 유저네임과 동일한 값이 DB에 있으면
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // 권한정보 설정
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        // 권한정보를 바탕으로 유저정보 생성
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();
        System.out.println("user@@@@@@@@@@@@@@@@@@@@@@@@ = " + user);
        return UserDto.from(userRepository.save(user));
    }

    // username을 파라미터로 받아서 해당 유저의 권한정보 반환
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    // 현재 securityContext에 저장되어 있는 유저네임에 해당하는 유저정보와 권한정보만 반환
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new RuntimeException("Member not found"))
        );
    }
}