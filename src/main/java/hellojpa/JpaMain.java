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
            //비영속
            Member member = new Member();
            member.setId(2l);
            member.setName("HelloB");
            //영속
            em.persist(member); // insert 쿼리, 바로 DB에 저장되는 것x -> 1차 캐쉬에 저장
            em.detached(member); // 회원 엔티티를 영속성 컨텍스트에서 분리
            em.remove(member); // 실제 영구 저장을 데이터베이스에서 삭제
            //회원 조회
            Member findMember = em.find(Member.class, 1l); //select 쿼리, 만약 없다면 영속성 컨텍스트에 저장
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            //회원 수정
            findMember.setName("HelloJPA"); //update 쿼리, 변경 감지

            //회원 삭제
            em.remove(findMember); //delete 쿼리

            // 플러쉬
            em.flush(); // 플러시는 영속성 컨텍스트 비우는것 x -> DB에 동기화

            // 준영속
            em.detach(member);
            em.clear(); // EntityManager 안의 모든 영속성 컨텍스트를 모두 초기화
            em.close(); // 영속성 컨텍스트 종료


            // 멤버 entity 대상으로 쿼리
            List<Member> result = em.createQuery("select m from Member as m ", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(5)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }*/

            //팀 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team); // 영속될때 pk 값이 세팅되고 영속됨

            //멤버 저장
            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team); // 연관관계의 주인이므로 등록이 가능하다.
            em.persist(member);

//            team.addMember(member);
//            team.getMembers().add(member);

//            em.flush();
//            em.clear();

            //조회
            Member findMember = em.find(Member.class, member.getId());

            //양방향 매핑
            System.out.println("=========================");
            System.out.println("findMember = " + findMember.getTeam().getName());

            Team findTeam = em.find(Team.class, team.getId()); // 1차 캐쉬
            List<Member> members = findTeam.getMembers();

            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }
            System.out.println("=========================");

            tx.commit(); //정상적이면 commit, DB에 저장되는 시점
        } catch (Exception e) {
            tx.rollback(); //예외 발생시 롤백
        } finally {
            em.close(); //작업이 끝나면 EntityManager 종료 -> 내부적으로 DB connection 반환
        }
        emf.close(); //자원 반환
    }
}
