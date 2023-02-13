package hello.advanced.trace.threadlocal.code;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FieldService {
    private String nameStore;

    // name을 nameStore저장하고 1초가 흐른 뒤 nameStore에서 값을 조회합니다.
    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}", name, nameStore);
        nameStore = name;
        sleep(1000);
        log.info("조회 nameStore={}",nameStore);
        return nameStore;
    }
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}