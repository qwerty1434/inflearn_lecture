package hello.proxy.pureproxy.proxy.code;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {
    private Subject target; // 프록시를 거쳐 호출하게 될 실제 객체
    private String cacheValue;
    public CacheProxy(Subject target) { // 실제 객체를 주입
        this.target = target;
    }
    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}