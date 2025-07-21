package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.common.BaseTimeEntity;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// jpa를 사용할 경우 Entity에 반드시 붙여야 하는 어노테이션이다.
// jpa의 Entity Manager 에게 객체를 위임하기 위한 어노테이션
// Entity Manager는 영속성 Context(Entity의 현재 상황/맥락)를 통해 DB 데이터를 관리한다. (코드가 우선)
@Entity
// Builder 어노테이션을 통해 유연하게 객체 생성 가능하다.
@Builder
public class Author extends BaseTimeEntity {
    @Id // pk 설정
    // IDENTITY : auto_increment, AUTO : id 생성 전략을 jpa에게 자동설정하도록 위임하는 것.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 컬럼에 별다른 설정이 없을 경우 default varchar(255).
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

//    @Column(name = "pw") // 되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있
    private String password;

    // 컬럼명에 캐멀케이스 사용 시, db에는 created_time으로 컬럼 생성
//    @CreationTimestamp // DB 설정에는 current_timestamp() 없음
//    private LocalDateTime createdTime;
//
//    @UpdateTimestamp
//    private LocalDateTime updatedTime;

    @Enumerated(EnumType.STRING)
    @Builder.Default // 빌더패턴에서 변수 초기화(디폴트 값)시 Builder.Default 어노테이션 필수
    private Role role = Role.USER;

    // OneToMany는 선택사항. 또한 default가 lazy
    // mappedBy에는 ManyToOne쪽에 변수명을 문자열로 지정. FK 관리를 반대편(post)쪽에서 한다는 의미 -> 연관 관계의 주인 설정
//    Cascade : 부모객체의 변화에 따라 자식객체가 같이 변함
//    1)persist: 부모객체까지 같이저장
//    2)remove: 부모객체까지 같이삭제
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // default가 lazy이지만 헷갈릴 수 있음 (ManyToOne이랑)
    @Builder.Default
    List<Post> postList = new ArrayList<>();

    @OneToOne(mappedBy = "author",fetch = FetchType.LAZY,cascade = CascadeType.ALL ,orphanRemoval = true)
    private Address address;

    public void updatePw(String password) {
        this.password = password;
    }

//    public AuthorDetailDto detailfromEntity() {
//        return AuthorDetailDto.builder()
//                .id(this.id)
//                .name(this.name)
//                .email(this.email)
//                .build();
//    }
    public AuthorListDto listfromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
