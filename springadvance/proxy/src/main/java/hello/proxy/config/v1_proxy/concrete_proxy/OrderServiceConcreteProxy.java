package hello.proxy.config.v1_proxy.concrete_proxy;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {
    private final OrderServiceV2 target;
    private final LogTrace logTrace;
    // 자식의 생성자는 항상 부모의 생성자를 super()로 호출해야 합니다.
    // super()로 호출하는 부분을 생략하면 기본생성자가 호출되는데 우리의 OrderServiceV2(부모)에는 기본생성자가 없습니다.
    // 그래서 super()는 생략이 불가능 합니다. 그런데 프록시는 부모객체의 기능을 직접 사용하지 않기 때문에 실제 값을 넣을 필요가 없습니다.
    // 구색을 맞추기 위해 null을 넣었습니다.
    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }
    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderService.orderItem()");
            //target 호출
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}