package hello.springmvc.basic.request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
@Slf4j
@RestController
public class RequestHeaderController {
    @RequestMapping("/headers")
    public String headers(HttpServletRequest request, //HttpServletRequest 받을 수 있습니다.
                          HttpServletResponse response, // HttpServletResponse 받을 수 있습니다.
                          HttpMethod httpMethod, // HttpMethod 받을 수 있습니다.
                          Locale locale, // Locale(언어정보) 받을 수 있습니다.
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host, // 특정 헤더 받을 수 있습니다)
                          @CookieValue(value = "myCookie", required = false) String cookie // 쿠키 받을 수 있습니다.
    ) {
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";
    }
}