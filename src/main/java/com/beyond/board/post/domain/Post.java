package com.beyond.board.post.domain;

import com.beyond.board.common.BaseTimeEntity;
import com.beyond.board.author.domain.Author;
import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private String category;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String contents;

    @Builder.Default
    private String delYn = "N";

    @Builder.Default
    private String appointment = "N";
    private LocalDateTime appointmentTIme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") // fk 관계성 설정
    private Author author;

    public void updateAppointment(String appointment) {
        this.appointment = appointment;
    }

}
