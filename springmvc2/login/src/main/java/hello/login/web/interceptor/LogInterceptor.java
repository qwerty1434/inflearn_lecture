package hello.login.web.interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logId";

    // Object handler가 넘어온다
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        // preHandle에서는 예외상황에서 안찍히기 때문에 afterCompletion에서 예외를 찍어야 합니다.
        // 이때 preHandle에서 선언한 uuid는 afterCompletion에 어떻게 넘길 수 있을까요?
        // 클래스 변수로 uuid를 선언하면 큰일납니다(싱글톤이기 때문에)
        // 이럴때 request.setAttribute를 사용하면 됩니다. -> afterCompletion에서 request.getAttribute로 받으면 됩니다.
        request.setAttribute(LOG_ID, uuid);

        // @RequestMapping을 사용하면 HandlerMethod,
        // 정적 리소스를 사용하면 ResourceHttpRequestHandler다
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }
        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; // true면 다음 단계(핸들러 어댑터가 호출되고 핸들러가 호출되고 ... )가 진행된다
    }

    // ModelAndView가 넘어온다
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    // Object handler, Exception ex가 넘어온다
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID); // preHandle에서 넣은 uuid값 받아오기
        log.info("RESPONSE [{}][{}]", logId, requestURI);
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}