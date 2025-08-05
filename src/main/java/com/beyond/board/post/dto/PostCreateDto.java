package com.beyond.board.post.dto;

import com.beyond.board.author.domain.Author;
import com.beyond.board.post.domain.Post;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDto {
    private String category;
    @NotEmpty(message = "제목 비어있음")
    private String title;
    private String contents;
    @Builder.Default
    private String appointment = "N";
    private String appointmentTime;

    public Post toEntity(Author author, LocalDateTime localDateTime) {
        return Post.builder()
                .category(this.category)
                .title(this.title)
                .contents(this.contents)
//                .authorId(this.authorId)
                .author(author)
                .delYn("N")
                .appointment(this.appointment)
                .appointmentTIme(localDateTime)
                .build();
    }
}
