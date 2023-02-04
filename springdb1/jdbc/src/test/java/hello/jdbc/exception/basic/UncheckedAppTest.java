package hello.jdbc.exception.basic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@Slf4j
public class UncheckedAppTest {
    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("ex", e);
        }
    }



    static class Controller {
        Service service = new Service();
        public void request() {
            service.logic();
        }
    }
    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();
        public void logic() {
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }
    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) { // 컴파일 익셉션을 런타임 익셉션으로 변환해서 던지는 게 좋습니다. 기존 예외에서 발생한 stacktrace도 같이 확인하기 위함입니다.
                throw new RuntimeSQLException(e);
            }
        }
        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }
    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }
    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException() {
        }
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}