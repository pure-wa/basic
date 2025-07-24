package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 기존 메서드들
    List<Post> findByAuthorId(Long authorId);
    List<Post> findByAuthor(Author author);

    // jpql을 사용한 일반 join
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

    // jpql을 사용한 fetch inner join
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();

    // 페이징 처리
    Page<Post> findAll(Specification<Post> specification,Pageable pageable);
    Page<Post> findAllByDelYn(String appointment, String delYn, Pageable pageable);



//    예약게시물기능 구현 페이징
    @Query("SELECT p FROM Post p WHERE p.delYn = :delYn AND " +
            "(p.appointment = 'N' OR (p.appointment = 'Y' AND p.appointmentTime <= :now)) " +
            "ORDER BY p.createdTime DESC")
    Page<Post> findPublishedPosts(@Param("delYn") String delYn, @Param("now") LocalDateTime now, Pageable pageable);

    // 🆕 예약 관련 메서드들
    List<Post> findByAppointment(String appointment);
    Page<Post> findByAppointmentAndDelYn(String appointment, String delYn, Pageable pageable);
}