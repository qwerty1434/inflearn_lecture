package study.datajpa.entity;
import lombok.*;

import javax.persistence.*;
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // proxy에서 사용할려면 protected이상이 필요합니다
@ToString(of = {"id", "username", "age"}) // 해당 변수들만 toString으로 출력하겠다는 의미입니다. team을 추가하면 순환참조가 일어날 수 있습니다.
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    public Member(String username) {
        this(username, 0);
    }
    public Member(String username, int age) {
        this(username, age, null);
    }
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }
    // 연관관계 편의 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}