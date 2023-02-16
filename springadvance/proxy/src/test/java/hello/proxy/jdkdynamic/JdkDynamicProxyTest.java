package hello.proxy.jdkdynamic;
import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Proxy;
@Slf4j
public class JdkDynamicProxyTest {
    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        // 프록시 생성 - Proxy.newProxyInstance()를 사용하면 됩니다.
        AInterface proxy = (AInterface)
                Proxy.newProxyInstance(AInterface.class.getClassLoader(), // 생성될 위치 지정
                        new Class[]{AInterface.class}, // 어떤 인터페이스 기반 프록시를 만들지 지정
                        handler); // 프록시가 호출할 로직
        proxy.call(); // 프록시의 call 호출
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        BInterface proxy = (BInterface)
                Proxy.newProxyInstance(BInterface.class.getClassLoader(),
                        new Class[]{BInterface.class}, handler);
        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}