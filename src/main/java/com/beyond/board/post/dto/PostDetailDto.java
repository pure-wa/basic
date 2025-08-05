package com.beyond.board.post.dto;

import com.beyond.board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailDto {
    private Long id;
    private String category;
    private String title;
    private String contents;
    private String authorEmail;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static PostDetailDto fromEntity(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .createdTime(post.getCreatedTime())
                .updatedTime(post.getUpdatedTime())
                .build();
    }
}
