package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository // 컴포넌트 스캔에 의해 자동으로 스프링 빈에 등록됩니다.
public class MemberRepository {

    @PersistenceContext // 스프링이 EntityManager를 만들어 자동으로 주입해 줍니다.
    private EntityManager em;

    /*
         // EntityManagerFactory를 자등으로 주입받아 사용하고 싶을 때
        @PersistenceUnit
        private EntityManagerFactory emf;
     */

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){
        // sql은 테이블을 대상으로, jpql은 엔티티를 대상으로 쿼리를 합니다.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
