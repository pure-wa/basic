package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.author.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // @Controller + @ResponseBody (화면 리턴 X)
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    // 회원가입
    @PostMapping("/create")
    // dto에 있는 validation어노테이션과 controller @Valid 한쌍
    public ResponseEntity<String> save(@Valid @RequestBody AuthorCreateDto authorCreateDto) {
//        try {
//            this.authorService.save(authorCreateDto);
//            return new ResponseEntity<>("ok", HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // 생성자 매개변수 body부분의 객체와 header부에 상태코드
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
        // controllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외처리가 가능.
        this.authorService.save(authorCreateDto);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    // 회원 목록 조회 (/list)
    @GetMapping("/list")
    public List<AuthorListDto> listAuthors() {
        List<AuthorListDto> authorListDto = this.authorService.findAll();
        System.out.println(authorListDto);
        return authorListDto;
    }

    // 회원 상세 조회 : id로 조회 (/detail/1)
    // 서버에서 별도의 try catch를 하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러 리턴.
    @GetMapping("/detail/{inputId}")
    public ResponseEntity<?> findById(@PathVariable Long inputId) { // AuthorDetailDto
//        AuthorDetailDto authorDetailDto = null;
//        try {
//            authorDetailDto = this.authorService.findById(inputId);
////            return new ResponseEntity<>(authorDetailDto, HttpStatus.OK); // authorDetailDto
//            return new ResponseEntity<>(new CommonDto(authorDetailDto,
//                    HttpStatus.CREATED.value(), "author created successfully!"), HttpStatus.OK);
//        } catch(NoSuchElementException e) {
//            e.printStackTrace();
////            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NO_CONTENT.value(),
//                    e.getMessage()), HttpStatus.NOT_FOUND);
//        }
        AuthorDetailDto authorDetailDto = null;
        authorDetailDto = this.authorService.findById(inputId);
        return new ResponseEntity<>(new CommonDto(authorDetailDto,
                HttpStatus.CREATED.value(), "author select successfully!"), HttpStatus.OK);
    }

    // 비밀번호 수정 (email, password -> json) (/updatepw)
    // get : 조회, post : 등록, patch : 부분 수정, put : 대체, delete :
    // patch method 사용
    @PatchMapping("/updatepw")
    public ResponseEntity<String> updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        try {
            this.authorService.updatePassword(authorUpdatePwDto);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    // 회원 탈퇴 (/author/1)
    // delete method 사용
    @DeleteMapping("/delete/{inputId}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long inputId) {
        try {
            authorService.delete(inputId);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
}
