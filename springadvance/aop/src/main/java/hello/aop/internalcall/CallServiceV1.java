package hello.aop.internalcall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 참고: 생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
 */
@Slf4j
@Component
public class CallServiceV1 {
    // 자기자신을 수정자 주입
    private CallServiceV1 callServiceV1;
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        // this.internal()이 아님
        callServiceV1.internal(); //외부 메서드 호출
    }
    public void internal() {
        log.info("call internal");
    }
}