package com.beyond.basic.b2_board.author.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorLoginDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
