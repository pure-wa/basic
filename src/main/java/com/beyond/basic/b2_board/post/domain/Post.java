package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.common.BaseTimeEntity;
import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
ㅈ
    @Column(length = 3000)
    private String contents;

    private String delYn;

    // FK 설정 시 ManyToOne 필수
    // ManyToOne에서는 default fetch.EAGER(즉시 로딩) : author객체를 사용하지 않아도 author 테이블로 쿼리 발생
    // 그래서 일반적으로 LAZY 설정 (지연로딩) : author객체를 사용하지 않는 한 author 객체로 쿼리발생 X
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") // fk 관계성 설정
    private Author author;
}
