package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    select * from post where author_id = ? and title = ?;
//     List<Post> findByAuthorIdAndTitle(Long author, String title);

//    select * from post where author_id = ? and title = ? order by createdTime desc;
//     List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long author, String title);



    List<Post> findByAuthorId(Long authorId);
    List<Post> findByAuthor(Author author);

    // jpql을 사용한 일반 join
    // jpa는 기본적으로 lazy로딩 지향, inner join으로 filtering은 하되 post객체만 조회 -> N+1문제 여전히 발생
    // raw쿼리 : select p.* from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

    // jpql을 사용한 fetch inner join
    // join시 post뿐만 아니라 author객체까지 한꺼번에 조립하여 조회 -> N+1문제 해결
    // raw쿼리 : select * from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();


    // paging 처리 + delYn 적용
    // Pageable (data.domain)
    // Page객체 안에 List<Post>, 전체페이지 수 등의 정보 포함
    // Pageable객체 안에는 페이지size, 페이지번호, 정렬기준 등이 포함.
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByDelYn(Pageable pageable, String delYn);


}
