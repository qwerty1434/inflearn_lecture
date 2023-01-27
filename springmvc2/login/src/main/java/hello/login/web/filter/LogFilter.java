package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*; // servlet의 Filter를 사용합니다.
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init 동작 확인");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter 동작 확인");
        // ServletRequest는 HttpServletRequest의 부모입니다.
        // ServletRequest에는 기능이 많지 않아 HttpServletRequest로 다운캐스팅 해줍니다.
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 사용자의 행동 정보
        String requestURI = httpRequest.getRequestURI();

        // 요청을 구분하기 위한 UUID
        String uuid = UUID.randomUUID().toString();

        try{
            // 모든 요청에 대해 로그를 남김
            log.info("REQUEST [{}][{}]",uuid,requestURI);
            // 다음 필터를 호출해줘야 합니다. (중요)
            chain.doFilter(request,response); // 다음 필터가 없으면 Servlet이 호출됩니다.
        }catch(Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]",uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy 동작 확인");
    }
}
