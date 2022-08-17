package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

//JPA 관리하는 객체, DB 테이블과 매핑해서 사용
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME") // 컬럼 매핑, nullable = false -> not null 제약 조건
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID") //주인, 값을 생성 및 수정 가능, N 쪽이 무조건 연관관계 주인
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public Long getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(Long teamId) {
//        this.teamId = teamId;
//    }


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); // ** 중요 **
        // mappedBy 이므로 등록, 변경을 위해서는 member.setTeam(team); 필요
        // 객체의 입장에서는 양쪽에 값을 넣어주는게 맞다.
        // 그래서 Member team 세팅하면 Team 객체 콜렉션에도 member 추가된다.

    }

}
