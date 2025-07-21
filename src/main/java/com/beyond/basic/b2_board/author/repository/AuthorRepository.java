package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// SpringDataJpa를 사용하기 위해서는 JpaRepository를 상속해야하고, 상속 시에 Entity명과 pk 타입을 지정해줘야 함.
// JpaRepository를 상속함으로써 JpaRepository의 주요 기능(각종 CRUD기능이 사전 구현) 상속
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // save, findAll, findById, delete 등은 사전에 구현
    // 그 외에 다른 컬럼으로 조회할 때는 findBy + 컬럼명 형식으로 선언만하면 실행시점에 자동으로 구현.

    Optional<Author> findByEmail(String email);



}
