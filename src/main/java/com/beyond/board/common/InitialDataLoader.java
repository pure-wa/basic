package com.beyond.board.common;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.domain.Role;
import com.beyond.board.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// CommandLineRunner를 구현함으로서 해당 컴포넌트가 스프링 빈으로 등록되는 시점에 run메서드 자동 실행
// 원래는 DB에 직접 insert 넣어줘야 함
// 자동화 작업
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(authorRepository.findByEmail("admin@naver.com").isPresent()) {
            return;
        }
        Author author = Author.builder()
                .email("admin@naver.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("12341234"))
                .build();
        authorRepository.save(author);
    }
}
