package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class) // Junit 실행할 때 스프링과 엮어서 실행한다는 의미입니다. junit5부터는 @SpringBootTest에 포함되어 있습니다.
@SpringBootTest // 스프링 부트를 띄운 상태로 테스트하겠다는 의미입니다.
@Transactional // 트랜잭셔널을 걸고 실행했다가 테스트가 끝나면 rollback합니다.
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(false) // rollback이 아닌 commit을 하기 때문에 em.persist(join메서드)할 때 insert문이 나갑니다.
    public void 회원가입() throws Exception{
        // given
        Member member = new Member();
        member.setName("Kim");

        // when
        // 테스트코드는 기본적으로 Commit이 아닌 Rollback을 하기 때문에 insert문이 나가지 않습니다. (Commit시점에 flush를 통해 DB에 insert됩니다)
        Long saveId = memberService.join(member);

        // then
        // 방금 저장할 때 사용한 멤버랑 repository에서 불러온 멤버가 같은건지 검증 (방금 멤버가 잘 저장됐는지 확인하는 것)
        // Transcational이기 때문에 같은 영속성 컨텍스트 내에서 같은 Id는 같은 엔티티임을 보장하기 때문에 True가 가능합니다.
        em.flush(); // Rollback어노테이션 없이 insert 쿼리를 보고싶을 때 사용합니다. Rollback이 true이기 때문에 여전히 DB에 값이 저장되지는 않습니다.
        Assert.assertEquals(member,memberRepository.findOne(saveId));

    }

    @Test // junit5에는 @Test에 expected 속성이 없습니다.
    public void 중복_회원_예외() throws Exception{
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        try{
            memberService.join(member2); // 예외가 발생해야 합니다.
        } catch(IllegalStateException e){
            return; // 테스트케이스가 성공합니다.
        }

        // then
        Assert.fail("예외가 발생해야 한다."); // 여기 오면 안되는 코드가 왔을 때 사용합니다.

    }

}