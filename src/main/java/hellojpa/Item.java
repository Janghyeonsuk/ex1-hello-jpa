package hellojpa;

import javax.persistence.*;

@Entity
// (JOINED , 조인전략) 각각의 테이블로 매핑
// (SINGLE_TABLE) 한 테이블로 전체 매핑, DTYPE 자동 생성, 쿼리가 한번, 조회도 조인x, 성능이 좋음
// (TABLE_PER_CLASS) 구현 클래스마다 테이블 전략, 사용 x ->
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn //DTYPE 나오도록 해주는 어노테이션, TABLE_PER_CLASS -> 필요 x,
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
