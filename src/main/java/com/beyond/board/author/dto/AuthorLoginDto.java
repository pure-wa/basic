package com.beyond.board.author.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorLoginDto {
    @NotEmpty(message = "이메일이 비어있습니다.")
    private String email;
    @NotEmpty(message = "비밀번호가 비어있습니다.")
    @Size(min = 8, message = "비밀번호가 짧습니다.")
    private String password;
}
