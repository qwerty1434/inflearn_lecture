package hello.exception.exhandler;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
public class ApiExceptionV2Controller {
    // 모두 ExControllerAdivce로 이동
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 해당 처리는 정상흐름이기 때문에 200이 반환됩니다. 이를 방지하기 위해 우리가 status값을 설정해 줍니다.
//    @ExceptionHandler(IllegalArgumentException.class) // IllegalArgumentException과 그 자식을 처리한다는 의미입니다.
//    public ErrorResult illegalExHandle(IllegalArgumentException e) {
//        log.error("[exceptionHandle] ex", e);
//        return new ErrorResult("BAD", e.getMessage()); //정상리턴으로 servlet 컨테이너까지 에러가 올라가지 않습니다.
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userExHandle(UserException e) { // 어노테이션에 Exception클래스를 넣는거 대신 인자로 특정 Exception클래스를 넘겨도 됩니다.
//        log.error("[exceptionHandle] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST); // ResponseEntity에 담아서 넘겨도 됩니다.
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandle(Exception e) { //Exception 및 Exception의 자식을 처리하기 때문에 위에서 처리하지 못한 모든 에러를 처리합니다.
//        log.error("[exceptionHandle] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }

    // 에러 발생 코드
    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}