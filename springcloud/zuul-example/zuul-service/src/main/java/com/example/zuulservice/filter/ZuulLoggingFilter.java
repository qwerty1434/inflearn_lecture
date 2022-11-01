package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j // Logger logger = LoggerFactory.getLogger(ZuulLoggingFilter.class);를 대신할 수 있음
@Component
public class ZuulLoggingFilter extends ZuulFilter {

    @Override
    public Object run() throws ZuulException { // 필터 동작
        log.info("**************** printing logs: ");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("**************** " + request.getRequestURI()); // 사용자 요청정보

        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    } // 필터 타입

    @Override
    public int filterOrder() {
        return 1;
    } // 필터 순서

    @Override
    public boolean shouldFilter() {
        return true;
    } // 필터 적용 여부
}
