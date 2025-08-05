package com.beyond.board.author.controller;

import com.beyond.board.author.dto.*;
import com.beyond.board.author.service.AuthorService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    // 회원가입 화면 반환
    @GetMapping("/create")
    public String createScreen() {
        return "author/author_register";
    }

    @GetMapping("/login")
    public String loginScreen() {
        return "author/author_login";
    }

    // 회원가입 요청 응답
    @PostMapping("/create")
    public String save(@Valid AuthorCreateDto authorCreateDto) {
        this.authorService.save(authorCreateDto);
        return "redirect:/";
    }

    // 로그인


    // 회원 목록 조회
    @GetMapping("/list")
    public String listAuthors(Model model) {
        List<AuthorListDto> authorListDto = this.authorService.findAll();
        model.addAttribute("authorList", authorListDto);
        return "author/author_list";
    }

    // 회원 상세 조회
    @GetMapping("/detail/{inputId}")
    public String findById(@PathVariable Long inputId, Model model) {
        AuthorDetailDto authorDetailDto = authorService.findById(inputId);
        model.addAttribute("authorDetail", authorDetailDto);
        return "author/author_detail";
    }

//    // 비밀번호 수정
//    @PatchMapping("/updatepw")
//    public ResponseEntity<String> updatePassword(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
//        try {
//            this.authorService.updatePassword(authorUpdatePwDto);
//            return new ResponseEntity<>("ok", HttpStatus.OK);
//        } catch(NoSuchElementException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
//        }
//    }
//
//    // 회원 탈퇴
//    @DeleteMapping("/delete/{inputId}")
//    public ResponseEntity<String> deleteAuthor(@PathVariable Long inputId) {
//        try {
//            authorService.delete(inputId);
//            return new ResponseEntity<>("ok", HttpStatus.OK);
//        } catch(NoSuchElementException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
//        }
//    }
}
