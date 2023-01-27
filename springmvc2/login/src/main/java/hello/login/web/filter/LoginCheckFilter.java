package hello.login.web.filter;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Slf4j
public class LoginCheckFilter implements Filter {

    // 필터의 적용을 받지 않는 페이지들
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false); // 기존에 있던 세션 가져오기
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);

                    //로그인으로 redirect
                    // redirectURL을 함께 보내서 유저가 로그인에 성공하면 home화면이 아니라 튕겨나간 페이지로 돌아가게 됨
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    return; //여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝!, chain.doFilter를 타지 못함
                }
            }

            chain.doFilter(request, response);

        } catch (Exception e) {
            throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }
    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) { // whitelist에 들어있지 않으면 로그인체크를 진행하라
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}