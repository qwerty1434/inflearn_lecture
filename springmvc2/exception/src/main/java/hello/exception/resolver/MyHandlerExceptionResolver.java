package hello.exception.resolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {

            if (ex instanceof IllegalArgumentException) { // IllegalArgumentException이 발생하면
                log.info("IllegalArgumentException resolver to 400");

                // 해당 에러는 해결했다 치고, 우리가 만든 400에러를 response.sendError로 보내겠다.
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                // 별다른 내용을 안넣으면 중간에 별다른 처리를 거치지 않고 정상return으로 WAS까지 갑니다. WAS는 response.sendError를 보고 400 에러를 터뜨립니다.
                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}