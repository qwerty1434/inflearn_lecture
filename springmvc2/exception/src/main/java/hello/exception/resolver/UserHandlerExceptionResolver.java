package hello.exception.resolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof UserException) { // UserException이면
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 500이 아닌 400에러로 나가도록 변경

                if ("application/json".equals(acceptHeader)) { // json형태의 요청이면
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult); // object를 json으로 변환

                    response.setContentType("application/json"); // 응답 타입이 json임을 명시
                    response.setCharacterEncoding("utf-8"); // 응답 인코딩이 utf8임을 명시
                    response.getWriter().write(result);
                    return new ModelAndView(); // 예외를 먹고 WAS까지 response가 정상적으로 전달됨
                } else { // json형태의 요청이 아니면
                    //TEXT 또는 HTML
                    return new ModelAndView("error/500");
                }
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}