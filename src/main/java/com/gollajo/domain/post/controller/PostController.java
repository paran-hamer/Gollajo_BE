package com.gollajo.domain.post.controller;

import com.gollajo.domain.post.dto.PostCreateRequest;
import com.gollajo.domain.post.service.PostService;
import com.gollajo.domain.s3.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
@Slf4j
public class PostController {

    private final AmazonS3Service amazonS3Service;
    private final PostService postService;

    @PostMapping(value = "/create",params = "type=1")
    public ResponseEntity<Long> createStringPost(@RequestBody PostCreateRequest postCreateRequest){
        Long postId = postService.createStringPost(postCreateRequest);
        return ResponseEntity.ok(postId);
    }
}
