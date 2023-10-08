package com.gollajo.domain.post.controller;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import com.gollajo.domain.post.dto.PostCreateRequest;
import com.gollajo.domain.post.dto.response.PostInfoResponse;
import com.gollajo.domain.post.dto.response.PostListResponse;
import com.gollajo.domain.post.service.PostService;
import com.gollajo.domain.vote.dto.VoteResultResponse;
import com.gollajo.domain.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
@Slf4j
public class PostController {

    private final PostService postService;
    private final VoteService voteService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<PostListResponse>> showAllPostList(){
        List<PostListResponse> postListResponses = postService.showAllPostList();
        return new ResponseEntity<>(postListResponses, HttpStatus.OK);
    }

    @PostMapping(value = "/create",params = "type=1")
    public ResponseEntity<Long> createStringPost(@RequestBody PostCreateRequest postCreateRequest){
        log.info(postCreateRequest.toString());
        //TODO : 실제 운영 서버에서는 jwt토큰으로 member불러와서 저장할 예정,
        Long memberId = 1L;
        Member member = memberService.findById(memberId);

        Long postId = postService.createStringPost(postCreateRequest,member);
        return ResponseEntity.ok(postId);
    }

    @PostMapping(value = "/create", params = "type=2")
    public ResponseEntity<Long> createImagePost(
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("request") PostCreateRequest postCreateRequest
    ) {
        log.info(postCreateRequest.toString());
        //TODO : 실제 운영 서버에서는 jwt토큰으로 member불러와서 저장할 예정
        Long memberId = 1L;
        Member member = memberService.findById(memberId);

        Long postId = postService.createImagePost(postCreateRequest, member, images);
        return ResponseEntity.ok(postId);
    }

    @GetMapping(value = "/cancel/{postId}")
    public ResponseEntity<Long> cancelPost(@PathVariable Long postId){
        Long memberId = 1L;
        Member member = memberService.findById(memberId);

        Long cancelPostId = postService.cancelPost(postId, member);
        return new ResponseEntity<>(cancelPostId, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable Long postId){

        Long memberId = 1L;
        Member member = memberService.findById(memberId);

        Long deletedPostId = postService.deletePost(member,postId);

        return new ResponseEntity<>(deletedPostId, HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostInfoResponse> showPostInfo(@PathVariable Long postId){
        PostInfoResponse postInfoResponse = postService.showPostInfo(postId);
        return new ResponseEntity<>(postInfoResponse,HttpStatus.OK);
    }

    @GetMapping("/{postId}/{optionId}")
    public ResponseEntity<List<VoteResultResponse>> vote
            (@PathVariable Long postId,
             @PathVariable Long optionId){

        Long memberId = 1L;
        Member member = memberService.findById(memberId);

        List<VoteResultResponse> voteResult =
                voteService.createVote(member, postId, optionId);

        return new ResponseEntity<>(voteResult,HttpStatus.OK);
    }



}
