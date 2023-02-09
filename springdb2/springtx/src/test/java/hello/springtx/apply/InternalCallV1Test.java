package hello.springtx.apply;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {
    @Autowired
    CallService callService;

    @TestConfiguration
    static class InternalCallV1Config {
        @Bean
        CallService callService() {
            return new CallService();
        }
    }

    @Slf4j
    static class CallService {
        public void external() { // 외부에서 호출됨, 트랜잭션 적용이 필요하지 않은 로직
            log.info("call external");
            printTxInfo();
            internal();
        }
        @Transactional
        public void internal() { // external에 의해 호출됨, 트랜잭션 적용이 필요한 로직
            log.info("call internal");
            printTxInfo();
        }
        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }

    @Test
    void internalCall() { // 외부에서 직접 호출 -> 트랜잭션 정상 적용
        callService.internal();
    }

    @Test
    void externalCall() { // 내부에서 internal 호출 -> 트랜잭션 적용 안됨
        callService.external();
    }
}