package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*
            //회원 등록
            Member member = new Member();
            member.setId(2l);
            member.setName("HelloB");
            em.persist(member); // insert 쿼리

            //회원 조회
            Member findMember = em.find(Member.class, 1l); //select 쿼리
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            //회원 수정
            findMember.setName("HelloJPA"); //update 쿼리

            //회원 삭제
            em.remove(findMember); //delete 쿼리
            */

            // 멤버 entity 대상으로 쿼리
            List<Member> result = em.createQuery("select m from Member as m ", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(5)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }

            tx.commit(); //정상적이면 commit
        } catch (Exception e) {
            tx.rollback(); //예외 발생시 롤백
        } finally {
            em.close(); //작업이 끝나면 EntityManager 종료 -> 내부적으로 DB connection 반환
        }
        emf.close(); //자원 반환
    }
}
