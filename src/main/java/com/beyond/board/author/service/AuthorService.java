package com.beyond.board.author.service;

import com.beyond.board.author.dto.*;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.author.domain.Author;
import com.beyond.board.post.domain.Post;
import com.beyond.board.post.repository.PostRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(AuthorCreateDto authorCreateDto) throws IllegalArgumentException {
        boolean isEmpty = this.authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent();
        if(isEmpty) throw new IllegalArgumentException("중복되는 이메일입니다.");
        if(authorCreateDto.getPassword().length() < 5) throw new IllegalArgumentException("비밀번호 길이가 짧습니다. (5자 이상)");

        String encodePassword = passwordEncoder.encode(authorCreateDto.getPassword());

        Author author = authorCreateDto.authorToEntity(encodePassword);

        this.authorRepository.save(author);
    }

    public Author doLogin(AuthorLoginDto dto) {
        Author author = this.authorRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));

        boolean check = passwordEncoder.matches(dto.getPassword(), author.getPassword());
        if(!check) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        return author;
    }


    public List<AuthorListDto> findAll() {
        return this.authorRepository.findAll().stream().map(a -> a.listfromEntity()).collect(Collectors.toList());
    }
    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));

        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);

        return dto;
    }

    public AuthorDetailDto findByAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));

        return AuthorDetailDto.fromEntity(author);
    }

    // 비밀번호 변경
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        String newPassword = authorUpdatePwDto.getPassword();
        String email = authorUpdatePwDto.getEmail();
        Optional<Author> optionalAuthor = authorRepository.findByEmail(email);
        Author author = optionalAuthor.orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        author.updatePw(newPassword);
    }

    // 회원 탈퇴
    public void delete(Long id) {
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        this.authorRepository.delete(author);
    }
}
