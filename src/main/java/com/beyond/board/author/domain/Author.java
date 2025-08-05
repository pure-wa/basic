package com.beyond.board.author.domain;

import com.beyond.board.common.BaseTimeEntity;
import com.beyond.board.author.dto.AuthorListDto;
import com.beyond.board.post.domain.Post;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class Author extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default // 빌더패턴에서 변수 초기화(디폴트 값)시 Builder.Default 어노테이션 필수
    private Role role = Role.USER;


    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // default가 lazy이지만 헷갈릴 수 있음 (ManyToOne이랑)
    @Builder.Default
    List<Post> postList = new ArrayList<>();

    public void updatePw(String password) {
        this.password = password;
    }

    public AuthorListDto listfromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
