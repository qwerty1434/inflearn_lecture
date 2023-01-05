package com.example.security1.config.auth;

import com.example.security1.Repository.UserRepository;
import com.example.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login"); 을 했기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadUserByUsername 함수가 실행됨
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // return한 값은 시큐리티 session의 Authentication 내부에 들어가게 됩니다.
    @Override
    // 매개변수 username은 loginForm에서 input의 name과 일치해야 함, 또는 SecurityConfig에서 usernameParameter로 이름을 설정해 줘야함
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;

    }
}
