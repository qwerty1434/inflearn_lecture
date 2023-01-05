package com.example.security1.config.auth;

import com.example.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 시큐리티가 /login을 주소 요청이 오면 낚아채서 로그인을 진행시킵니다.
 * 로그인 진행이 완료가 되면 session에 넣어줘야 합니다.
 * 이때 security 자신만의 세션에다가 Security ContextHolder라는 키값으로 정보를 저장시킵니다.
 * 이때 여기에 들어갈 수 있는 정보의 타입은 Authentication 타입의 객체여야 합니다.
 * Authentication안에 User정보가 있어야 하는데 User오브젝트의 타입은 UserDetails타입의 객체여야 합니다.
 * 즉, Security Session에 저장하는 정보의 타입은 Authentication 타입의 객체여야 하고, Authentication에 들어가는 정보는 UserDetails라는 타입의 정보여야 합니다.
 */
public class PrincipalDetails implements UserDetails {

    private User user; // Composition
    public PrincipalDetails(User user){
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳
    // user.getRole()을 return해야 되는데 Type이 안맞기 때문에 감싸서 반환해 준다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole(); // 우리가 반환할 정보
            }
        });
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 1년동안 회원이 로그인을 안하면 휴면계정으로 만들고자 할 때 사용
        return true;
    }
}
