package hello.proxy.cglib;
import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
@Slf4j
public class CglibTest {
    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer(); //cglib를 만드는 코드
        enhancer.setSuperclass(ConcreteService.class); // 인터페이스가 아닌 슈퍼클래스를 활용해 cglib를 만듦
        enhancer.setCallback(new TimeMethodInterceptor(target)); // TimeMethodInterceptor를 넣음
        ConcreteService proxy = (ConcreteService)enhancer.create(); // create로 프록시 생성
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
        proxy.call();
    }
}