package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 컴포넌트 스캔의 대상이 됩니다.
@Transactional(readOnly = true) // jpa의 모든 데이터변경, 로직은 트랜잭션 안에서 실행되야 합니다. service단위로 Transaction을 걸면 하위 메서드에는 자동으로 모두 적용됩니다. 조회하는 메서드에 @Transactional(readOnly = true)를 쓰면 성능이 조금 더 최적화됩니다.

public class MemberService {

    /*
     * 필드 인젝션
     */
//    @Autowired
//    private MemberRepository memberRepository;

    /* 세터 인젝션
     * 주입되는 MemberRepository를 변경하기 쉬워 테스트가 편리합니다.
     * 런타임 시점에 누군가가 변경할 수 있다는 치명적인 단점이 있습니다.(애플리케이션이 실행되고 있을 때 바뀔 가능성이 있다는 얘기)
     */
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    /*
     * 생성자 주입
     */
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    @Transactional // 전역의 readOnly = true를 적용하지 않습니다. (기본속성이 readOnly = false)
    public Long join(Member member){
        // 중복회원 검사
        validateDuplicateMember(member);
        memberRepository.save(member); // em.persist시점에 영속성 컨텍스트에 member객체를 올립니다. 이때 영속성 컨텍스트의 Id값은 해당 객체의 PK값입니다. @GeneratedValue는 PK값이 생성됨이 보장되기 때문에 영속성 컨텍스트에 바로 넣을 수 있습니다.
        return member.getId();
    }

    // 두 유저가 동시에 회원가입을 신청하면 중복검사에 실패할 수도 있습니다. -> member의 name을 unique 제약조건을 걸면 조금 더 안전해 집니다.
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    // 변경감지를 활용한 값 업데이트
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
