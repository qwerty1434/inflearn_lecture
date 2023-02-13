package hello.advanced.app.v1;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {
    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace; // 추가

    @GetMapping("/v1/request")
    public String request(String itemId) {

        TraceStatus status = null; // catch에도 status를 넘기기 위해 밖으로 빼줌
        try{
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        }catch(Exception e){
            trace.exception(status, e);
            // 예외를 꼭 다시 던져주어야 합니다.
            // 로그 기능때문에 여기서 예외를 먹어버리면 기존 로직과 흐름이 달라집니다(예외를 처리한 정상 흐름이 되어버림).
            throw e;
        }
    }
}