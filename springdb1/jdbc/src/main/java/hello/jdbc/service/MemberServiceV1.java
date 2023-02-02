package hello.jdbc.service;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;
import java.sql.SQLException;
@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        // from의 돈을 깎아서
        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember); // 계좌이체 실패 케이스를 구현하기 위해 exception을 던지는 상황 추가
        // to의 돈을 증가시킴
        memberRepository.update(toId, toMember.getMoney() + money);
    }
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}