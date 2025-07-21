package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// mybatis repository로 만들 때 필요한 어노테이션
@Mapper // 이 껍데기와 구현체와 연결해주겠다. 를 의미 (xml 파일과)
public interface AuthorMybatisRepository {
    // 회원가입
    void save(Author author);

    // 회원 목록 조회
    List<Author> findAll();

    // 회원 상세 조회
    Optional<Author> findById(Long id);
    Optional<Author> findByEmail(String email);

    // 회원 탈퇴
    void delete(Long id);
}
