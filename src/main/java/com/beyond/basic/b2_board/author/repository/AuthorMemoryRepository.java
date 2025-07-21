package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository // Component포함(싱글톤)
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();
    public static Long id = 1L;

    // 회원가입
    public void save(Author author) {
        this.authorList.add(author);
        id++;
    }

    // 회원 목록 조회
    public List<Author> findAll() {
        return this.authorList;
    }

    // 회원 상세 조회
    public Optional<Author> findById(Long id) {
        return authorList.stream().filter(a -> a.getId().equals(id)).findFirst();
    }
    public Optional<Author> findByEmail(String email) {

        return null;
    }

    // 회원 탈퇴
    public void delete(Long id) {
        // id 값으로 요소의 index값을 찾아 삭제
        Author author = null;
        for(Author a : authorList) {
            if(a.getId().equals(id)) {
                author = a;
                break;
            }
        }
        authorList.remove(author);
    }


    // 검증 (true : 이메일 존재 / false : 이메일 없음)
    public boolean isValidEmail(String email) {
        Optional<Author> isValid =  authorList.stream().filter(a -> a.getEmail().equals(email)).findFirst();

        if(isValid.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    public Optional<Author> getAuthorByEmail(String email) {
        Optional<Author> optionalAuthor = authorList.stream().filter(a -> a.getEmail().equals(email)).findFirst();
        return optionalAuthor;
    }
}
