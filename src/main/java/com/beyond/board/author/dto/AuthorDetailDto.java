package com.beyond.board.author.dto;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthorDetailDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Integer postCount;
    private LocalDateTime createdTime;

    public static AuthorDetailDto fromEntity(Author author) {
        return AuthorDetailDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .role(author.getRole())
                .postCount(author.getPostList().size())
                .createdTime(author.getCreatedTime())
                .build();
    }
}
