package hello.exception;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 404에러가 발생하면 error-page/404로 이동하라는 의미입니다.
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/errorpage/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        // 런타임 익셉션 또는 그 자식 예외가 발생하면 error-page/500으로 이동하라는 의미입니다.
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/errorpage/500");

        // 에러를 등록해 줍니다
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}