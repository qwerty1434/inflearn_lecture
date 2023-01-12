package com.example.jwt.config.auth;

import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// localhost:8080/login을 요청했을 때 동작해야 하는데 우리는 formLogin을 disable해서 동작하지 않음, 필터에서 이걸 호출해야 함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        System.out.println("PrincipalDetailsService의 loadUserByUsername호출됨");
        User userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }

}
