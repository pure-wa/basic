package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Component로도 대체 가능 (트랜잭션 처리가 없는 경우에만)
@Transactional // 스프링에서 메서드 단위로 트랜잭션 처리를 하고, 만약 예외(unchecked) 발생 시 자동으로 롤백 처리 지원
@RequiredArgsConstructor
public class AuthorService {

    // 의존성 주입(DI) 방법 1. Autowired 어노테이션 사용 -> 필드 주입
//    @Autowired
//    private AuthorRepository authorRepository;

    // 의존성 주입(DI) 방법 2. 생성자 주입 방식 (가장 많이 쓰는 방식)
    // 장점1) final을 통해 상수로 사용가능 (안전성 향상)
    // 장점2) 다형성 구현 가능 (인터페이스/상속)
    // 장점3) 순환참조 방지 (컴파일 타임에 check) / 이제는 둘 다 방지되긴 함. 순환참조 용어만 확인
//    private final AuthorRepositoryInterface authorMemoryRepository;
//    // 객체로 만들어지는 시점에 스프링에서 authorRepository 객체를 매개변수로 주입
//    @Autowired // 생성자가 하나밖에 없을 때에는 @Autowired 생략 가능
//    // 주입의 객체가 바뀌면 AuthorMemoryRepository 타입만 바꿔준다.
//    public AuthorService(AuthorMemoryRepository authorMemoryRepository) {
//        this.authorMemoryRepository = authorMemoryRepository;
//    }

    // 의존성 주입(DI) 방법 3. RequiredArgs 어노테이션 사용
    // -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동 생성 해주는 어노테이션
    // 다형성 설계는 불가능
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;



    // 회원가입 (객체 조립은 서비스 담당)
    public void save(AuthorCreateDto authorCreateDto) throws IllegalArgumentException {
        // 이메일 중복 검증
//        boolean isValidEmail = this.authorRepository.isValidEmail(authorCreateDto.getEmail());
//        if(isValidEmail) throw new IllegalArgumentException("중복되는 이메일입니다.");
        boolean isEmpty = this.authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent();
        if(isEmpty) throw new IllegalArgumentException("중복되는 이메일입니다.");

        // 비밀번호 길이 검증
        if(authorCreateDto.getPassword().length() < 5) throw new IllegalArgumentException("비밀번호 길이가 짧습니다. (5자 이상)");

        // 회원가입 완료
//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(),
//                authorCreateDto.getPassword());
        // authorToEntity()를 통해 한 번에 변환
        Author author = authorCreateDto.authorToEntity();
        this.authorRepository.save(author);



//        cascading 테스트 : 회원이 생성될때, 곧바로 "가입인사"글을 생성하는 상황
//        방법2가지
//        방법1. 직접 POST객체 생성 후 저장
        Post post = Post.builder()
                .title("안녕하세요")
                .contents(authorCreateDto.getName()+"입니다. 반갑습니다.")
//                author 객체가 db에 save되는 순간 엔티티매니저와 영속성컨텍스트에 의해 author 객체에도 id값 생성
                .author(author)
                .build();
//        postRepository.save(post);
//        방법2. cascade옵션 활용
        author.getPostList().add(post);

    }

    public List<AuthorListDto> findAll() {
//        List<AuthorListDto> authorListDto = new ArrayList<>();
//        List<Author> authorList = this.authorMemoryRepository.findAll();
//        for(Author a : authorList) {
//            authorListDto.add(new AuthorListDto(a.getId(), a.getName(), a.getEmail()));
//
//        }
//        return authorListDto;

        return this.authorRepository.findAll().stream().map(a -> a.listfromEntity()).collect(Collectors.toList());
    }

    // orElseThrow -> NoSuchElement
    // 회원 상세 조회
    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
//        return new AuthorDetailDto(author.getId(), author.getName(), author.getEmail());
//        return author.detailfromEntity();
//        AuthorDetailDto dto = author.detailfromEntity();

        // 연관관계 설정 없이 직접 조회해서 count값 찾는 경우
        List<Post> postList = postRepository.findByAuthor(author);
//        List<Post> postList2 = postRepository.findByAuthorId(id);
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author, postList.size());

        // OneToMany 연관관계 설정을 통해 count값 찾는 경우
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);

        return dto;
    }

    // 비밀번호 변경
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        String newPassword = authorUpdatePwDto.getPassword();
        String email = authorUpdatePwDto.getEmail();
//        Optional<Author> optionalAuthor = authorRepository.getAuthorByEmail(email);
        Optional<Author> optionalAuthor = authorRepository.findByEmail(email);
        Author author = optionalAuthor.orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        // dirty checking : 객체를 수정한 후에 별도의 updqte쿼리 발생시키지 않아도, 영속성 컨텍스트에 의해 객체 변경사항 자동 DB 반영
        author.updatePw(newPassword); // jpa -> 객체가 수정되면 자동으로 쿼리가 나간다.
    }

    // 회원 탈퇴
    public void delete(Long id) {
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        this.authorRepository.delete(author);
    }
}
