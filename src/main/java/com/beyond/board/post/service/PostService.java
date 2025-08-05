package com.beyond.board.post.service;

import com.beyond.board.author.domain.Author;
import com.beyond.board.author.repository.AuthorRepository;
import com.beyond.board.post.domain.Post;
import com.beyond.board.post.dto.PostCreateDto;
import com.beyond.board.post.dto.PostDetailDto;
import com.beyond.board.post.dto.PostListDto;
import com.beyond.board.post.repository.PostRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    public void save(PostCreateDto dto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();

        Author author = authorRepository.findByEmail("admin@naver.com").orElseThrow(() -> new EntityNotFoundException("없는 사용자 입니다."));

        LocalDateTime appointmentTime = null;
        if(dto.getAppointment().equals("Y")) {
            if(dto.getAppointmentTime() == null || dto.getAppointmentTime().isEmpty()) {
                throw new IllegalArgumentException("시간정보가 비어져 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(dto.getAppointmentTime(),dateTimeFormatter);
        }

        postRepository.save(dto.toEntity(author, appointmentTime));
    }

    public Page<PostListDto> findAll(Pageable pageable) {
        Page<Post> postList = postRepository.findAll(pageable);
        return postList.map(a -> PostListDto.fromEntity(a));
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 ID입니다."));
        return PostDetailDto.fromEntity(post);
    }
}
