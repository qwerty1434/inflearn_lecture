package com.example.ex1hellojpa.hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // JPA의 모든 작업은 Transaction 단위로 실행되어야 한다. Transaction이 시작되지 않은 상태에서 데이터 저장같은거 불가능
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");
            em.persist(member); // member저장
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
