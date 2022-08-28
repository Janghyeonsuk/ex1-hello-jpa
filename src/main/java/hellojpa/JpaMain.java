package hellojpa;

        import org.hibernate.Hibernate;

        import javax.persistence.EntityManager;
        import javax.persistence.EntityManagerFactory;
        import javax.persistence.EntityTransaction;
        import javax.persistence.Persistence;
        import java.util.List;
        import java.util.Set;

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
            // 프록시 객체 초기화란 프록시 객체가 실제 사용될 때 데이터베이스를 조회해서 실제 엔티티 객체를 생성하는 것
            Hibernate.initialize(refMember);
            // member.getName(); -> 강제 호출로 초기화

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamB);
            em.persist(member2);

            em.flush();
            em.clear();

            Member m = em.getReference(Member.class, member1.getId());
            System.out.println("m = " + m.getTeam().getClass());

            System.out.println("============");
            System.out.println("m = " + m.getTeam().getName());; // team.getName()을 할떄 프록시 초기화
            System.out.println("============");

            // fetch join -> 런타임에 동적으로 원하는 엔티티를 선택해서 한번에 가져옴
            List<Member> members = em.createQuery("select  m from Member m join fetch m.team", Member.class)
                    .getResultList();

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            em.remove(findParent); // 부모 삭제 -> 자식 삭제

            Address address = new Address("city", "street", "10000");

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setHomeAddress(address);
            em.persist(member1);

            //값 복사
            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setHomeAddress(copyAddress);
            em.persist(member2);

            // 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함 -> 값을 복사해서 사용
            member1.getHomeAddress().setCity("newCity"); // member1의 city만 바꾸려했는데 member2의 city도 변경

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

            member.getAddressHistory().add(new Address("old1", "street", "10000"));
            member.getAddressHistory().add(new Address("old2", "street", "10000"));

            // 일대다로 테이블로 매핑
            member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("========START========");
            Member findMember = em.find(Member.class, member.getId());

            // 값 조회
            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address.getCity() = " + address.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

            // homeCity -> newCity 값 타입 수정 -> 통으로 교체
//            findMember.getHomeAddress().setCity("newCity");
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity",a.getStreet(), a.getZipcode()));

            // 치킨 -> 한식 / String 갈아 끼우는게 아닌 삭제후 추가
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // 값 타입 컬렉션에 변경 발생 -> 주인 엔티티와 연관된 모든 데이터 삭제 -> 현재 값을 모두 다시저장
            findMember.getAddressHistory().remove(new Address("old1", "street", "10000"));
            findMember.getAddressHistory().add(new Address("newCity1", "street", "10000"));
*/

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homeCity", "street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

            em.persist(member);

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
