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

            //팀 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team); // 영속될때 pk 값이 세팅되고 영속됨

            //멤버 저장
            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            //조회
            Member findMember = em.find(Member.class, member.getId());
            //양방향 매핑
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            
            //참조를 사용해서 연관관계 매핑
//            Team findTeam = findMember.getTeam();
//            System.out.println("findTeam.getName() = " + findTeam.getName());

            tx.commit(); //정상적이면 commit, DB에 저장되는 시점
        } catch (Exception e) {
            tx.rollback(); //예외 발생시 롤백
        } finally {
            em.close(); //작업이 끝나면 EntityManager 종료 -> 내부적으로 DB connection 반환
        }
        emf.close(); //자원 반환
    }
}
