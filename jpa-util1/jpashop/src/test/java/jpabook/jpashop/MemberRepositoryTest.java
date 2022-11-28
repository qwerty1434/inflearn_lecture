package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional // 없으면 에러가 발생합니다.
    @Rollback(false) // DB에서 데이터가 변경될걸 확인하고 싶을 때
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); // ==비교, 같습니다.

        // 같은 트랜잭션이기 때문에 같은 영속성 컨텍스트의 관리를 받습니다.
        // 같은 영속성 컨텍스트에서는 id값이 같으면 같은 엔티티로 취급해 ==을 true로 반환합니다.
        System.out.println("findMember == member = " + (findMember == member));

    }

}