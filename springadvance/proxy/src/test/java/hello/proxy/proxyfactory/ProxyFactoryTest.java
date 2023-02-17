package hello.proxy.proxyfactory;
import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {
    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl(); // 인터페이스가 존재하는 케이스
        ProxyFactory proxyFactory = new ProxyFactory(target); // 프록시 팩토리 생성
        proxyFactory.addAdvice(new TimeAdvice()); // 프록시 팩토리에 우리가 적용할 Advice 전달
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy(); // 프록시 팩토리로 프록시 생성
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // com.sun.proxy.$Proxy13 -> JDK 동적 프록시가 적용된걸 확인 가능

        proxy.save(); // 로직 실행

        // AopUtils: 프록시 팩토리로 생성한 프록시에 대해 동작하는 기능들(직접 만든 프록시에는 사용 불가)
        // AopUtils.isAopProxy(proxy): 프록시인지 확인
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        // AopUtils.isJdkDynamicProxy(proxy): JDK 동적 프록시인지 확인
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        // AopUtils.isCglibProxy(proxy): CGLIB 동적 프록시인지 확인
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); //중요
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
        proxy.save();
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}