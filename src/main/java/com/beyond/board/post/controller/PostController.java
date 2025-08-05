package com.beyond.board.post.controller;

import com.beyond.board.post.dto.PostCreateDto;
import com.beyond.board.post.dto.PostDetailDto;
import com.beyond.board.post.dto.PostListDto;
import com.beyond.board.post.service.PostService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/create")
    public String postCreateScreen() {
        return "post/post_register";
    }

    @PostMapping("/create")
    public String create(@Valid PostCreateDto dto) {
        postService.save(dto);
        return "redirect:/post/list";
    }

    @GetMapping("/list")
    public String postList(@PageableDefault(size=5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<PostListDto> postListDtoPage = postService.findAll(pageable);
        model.addAttribute("postList", postListDtoPage);
        return "post/post_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PostDetailDto dto = postService.findById(id);
        model.addAttribute("post", dto);
        return "post/post_detail";
    }

}
