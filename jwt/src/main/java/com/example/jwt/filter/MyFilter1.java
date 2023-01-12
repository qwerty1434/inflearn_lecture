package com.example.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰을 만들었다고 가정해서 우리가 만든 토큰이 헤드에 담겨있다면 정상 진행
        if(req.getMethod().equals("POST")){
            String headerAuth = req.getHeader("Authorization");
            if(headerAuth.equals("우리토큰")){
                chain.doFilter(request,response);
            }else{
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }

    }
}
