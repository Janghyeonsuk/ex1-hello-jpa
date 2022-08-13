package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//JPA 관리하는 객체, DB 테이블과 매핑해서 사용
@Entity
//@Table(name = "MBR") //테이블명
public class Member {

    @Id //pk
    private Long id;
    @Column(unique = true, length = 10)
    private String name;


    public Member() {

    }
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
