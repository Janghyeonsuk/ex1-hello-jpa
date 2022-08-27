package hellojpa;

        import org.hibernate.Hibernate;

        import javax.persistence.EntityManager;
        import javax.persistence.EntityManagerFactory;
        import javax.persistence.EntityTransaction;
        import javax.persistence.Persistence;

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
            member.setId(1l);
            member.setName("HelloA");
            //영속
            em.persist(member); // insert 쿼리, 바로 DB에 저장되는 것x -> 1차 캐쉬에 저장
            em.detached(member); // 회원 엔티티를 영속성 컨텍스트에서 분리
            //회원 조회
            Member findMember = em.find(Member.class, 1l); //select 쿼리, 만약 없다면 DB 조회후 영속성 컨텍스트에 저장
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
            }

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

*/

            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            em.flush();
            em.clear();

            // 프록시
            Member reference = em.getReference(Member.class, member1.getId());
            // 프록시는 실제 클래스를 상속받아서 만들어진다, 실제와 겉이 같아서 사용자는 실제인지 프록시인지 구분x
            // 프록시 객체는 처음 사용할 때 한번만 초기화, 프록시 엔티티가 실제 엔티티로 바뀌는게 아닌 프록시 엔티티가 실제 엔티티에 접근 가능
            System.out.println("reference = " + reference.getClass());
            System.out.println("before reference.getId() = " + reference.getId());
            System.out.println("reference.getUsername() = " + reference.getUsername());// target.getUsername();
            System.out.println("after reference = " + reference.getClass()); // 그대로 프록시

            // 프록시가 넘어올지, 실제가 올지 모르므로 타입 비교를 ==으로 하면 안되고 instance of 사용
            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.getReference(Member.class, member1.getId());
            System.out.println("m1==m2: " + (m1.getClass() == m2.getClass()));
            System.out.println("m1 == member: " + (m1 instanceof Member));
            System.out.println("m2 == member: " + (m2 instanceof Member));

            Member refMember = em.getReference(Member.class, member1.getId());
            System.out.println("refMember = " + refMember.getClass()); //Proxy

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember.getClass());
            // JPA 에서는 == 을 참으로 보장해주기 위해 둘을 맞춘다.
            // 두번 getReference -> 둘다 프록시 반환
            // 이미 영속성 컨텍스트에 있으면 프록시가 아닌 실제 엔티티 반환
            // 프록시에서 한번을 조회하면 find 또한 프록시를 반환
            System.out.println("(refMember == findMember) = " + (refMember == findMember));
            // 프록시 인스턴스 초기화 여부 확인
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            // 프록시 강제 초기화
            Hibernate.initialize(refMember);

            tx.commit(); //정상적이면 commit, DB에 저장되는 시점
        } catch (Exception e) {
            tx.rollback(); //예외 발생시 롤백
            e.printStackTrace();
        } finally {
            em.close(); //작업이 끝나면 EntityManager 종료 -> 내부적으로 DB connection 반환
        }
        emf.close(); //자원 반환
    }

}
