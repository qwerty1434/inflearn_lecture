package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// UsernamePasswordAuthenticationFilter은 스프링 시큐리티에 있는 필터
// 해당 필터는 /login에 post로 username과 password를 전송하면 동작함

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");
        // 1. username과 password를 받음
        try{
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(),User.class);
            System.out.println("user = " + user);
            // 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());

            // 2. authenticationManager로 로그인을 시도하면 PrincipalDetailService가 호출됨 -> 내부의 loadUserByUsername이 실행됨
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. PrinciaplDetails를 세션에 담고 (권환 관리를 안하면 세션에 담을 필요 없음)
            // authentication의 getPrincipal이 정상적으로 작동함 -> authentication객체가 세션에 저장되어 있다는 뜻 -> 로그인이 정상적으로 되었다는 뜻
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername()+"@@@@@@@@@@@@@@@@@@@@@@@");
            // 4. JWT토큰을 만들어서 응답해주면 됨 -> successfulAuthentication에서 진행하면 됨
            return authentication;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨: 인증이 완료되었다는 뜻");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("cos") // 토큰이름
                .withExpiresAt(new Date(System.currentTimeMillis()+600000*10))
                .withClaim("id",principalDetails.getUser().getId()) // 넣고싶은 key - value를 마음대로 넣으면 됨
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos")); // 시크릿 값

        // 토큰을 response의 header에 담음
        response.addHeader("Authorization","Bearer "+jwtToken);

        super.successfulAuthentication(request,response,chain,authResult);
    }
}
