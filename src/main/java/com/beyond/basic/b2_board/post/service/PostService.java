package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    @Autowired // 생성자가 한개일때는 안붙여도된다.
    public PostService(PostRepository postRepository, AuthorService authorService, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto postCreateDto){
        // authorId가 실제 있는지 없는지 검증.
//        postRepository.find(postCreateDto.getAuthorId())
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // subject : email
        Author author = authorRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("존재하지 않는 게시글입니다."));
        LocalDateTime appointmentTime = null;

        if(postCreateDto.getAppointment().equals("Y")){
            if(postCreateDto.getAppointmentTime()==null || postCreateDto.getAppointmentTime().isEmpty()){
                throw new IllegalArgumentException("시간 정보가 비어져 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(postCreateDto.getAppointmentTime(), dateTimeFormatter);
        }

        postRepository.save(postCreateDto.toEntity(author, appointmentTime));
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("존재하지 않는 게시글입니다.")
        );
        // 엔티티간의 관계성 설정을 하지 않았을 때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 회원입니다."));
//        authorService.getOrElseThrow(post.getAuthorId())
//        return PostDetailDto.fromEntity(post,post.getAuthor());

        //엔티티간의 관계성 설정을 통해 Author 객체를 쉽게 조회하는 경우
        //        직접 조회해서 count 찾는 경우
//        return PostDetailDto.fromEntity(post, author);
//     OneToMany 관계를 설정해 count값 찾는 경우
        return PostDetailDto.fromEntity(post);
    }

    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto dto){
        // postList를 조회할 떄 참조관계에 있는 author까지 조회하게 되므로 , N(author쿼리) + 1(post 쿼리) 문제 발생
        // jpa는 기본 방향성이 fetch lazy 이므로, 참조하는 시점에 쿼리를 내보내게 되어 N+1 Join 문을 만들어주지 않고 문제 발생
//        List<Post> postList = postRepository.findAll(); // 일반 findAll
//        List<Post> postList = postRepository.findAllJoin(); // 일반 inner join
//        List<Post> postList = postRepository.findAllFetchJoin(); // inner join fetch

        // 검색을 위해 Specification 객체 스프링에서 제공
        // 페이지 처리 findAll 호출
        // specification 객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로, 쿼리를 쉽게 생성
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // Root : 엔티티의 속성을 접근하기 위한 객체 CriteriaBuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("delYn"),"N"));
                predicateList.add(criteriaBuilder.equal(root.get("appointment"),"N"));
                if(dto.getCategory()!=null){
                    predicateList.add(criteriaBuilder.equal(root.get("category"),dto.getCategory()));
                }
                if(dto.getTitle()!=null){
                    predicateList.add(criteriaBuilder.like(root.get("title"),"%"+dto.getTitle()+"%"));
                }

                Predicate[] predicatesArr = new Predicate[predicateList.size()];
                for(int i=0; i<predicateList.size(); i++){
                    predicatesArr[i] = predicateList.get(i);
                }

//                위의 검색 조건들을 하나(한 줄) 의 Predicate 객체로 만들어서 return
                Predicate predicate = criteriaBuilder.and(predicatesArr);
                return predicate;
            }
        };
        Page<Post> postList = postRepository.findAll(specification,pageable);
//              return  postList.stream().map((a)->new PostListDto().fromEntity(a)).collect(Collectors.toList());
        return  postList.map((a)->new PostListDto().fromEntity(a));
    }
}