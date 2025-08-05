package com.beyond.board.post.dto;

import com.beyond.board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListDto {
    private Long id;
    private String category;
    private String title;
    private String authorEmail;

    public static PostListDto fromEntity(Post post) {
        return PostListDto.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }
}
