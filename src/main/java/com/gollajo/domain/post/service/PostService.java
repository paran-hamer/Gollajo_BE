package com.gollajo.domain.post.service;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.exception.AccountExceptionHandler;
import com.gollajo.domain.account.repository.AccountRepository;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.repository.MemberRepository;
import com.gollajo.domain.post.dto.PostCreateRequest;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.PostBody;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import com.gollajo.domain.post.exception.PostExceptionHandler;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.s3.AmazonS3Service;
import com.gollajo.global.exception.CustomException;
import com.gollajo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    private final AmazonS3Service amazonS3Service;

    private final PostExceptionHandler postExceptionHandler;
    private final AccountExceptionHandler accountExceptionHandler;


    public Long createStringPost(PostCreateRequest request, Member member){

        postExceptionHandler.createPostException(request,member);

        PostBody createdPostBody = PostBody.builder()
                .postType(PostType.STRING_OPTION)
                .title(request.title())
                .content(request.content())
                .maxVotes(request.maxVotes())
                .pointPerVote(request.pointPerVote())
                .expirationDate(request.expirationDate())
                .build();

        Post post = Post.builder()
                .postBody(createdPostBody)
                .member(member)
                .postState(PostState.STATE_GENERATING)
                .build();

        post.mapPostStringOption(post,request.optionContent());

        postRepository.save(post);

        return post.getId();

    }

    public Long createImagePost(PostCreateRequest request, Member member, List<MultipartFile> images){

        postExceptionHandler.createPostException(request,member);

        PostBody createdPostBody = PostBody.builder()
                .postType(PostType.IMAGE_OPTION)
                .title(request.title())
                .content(request.content())
                .maxVotes(request.maxVotes())
                .pointPerVote(request.pointPerVote())
                .expirationDate(request.expirationDate())
                .build();

        Post post = Post.builder()
                .postBody(createdPostBody)
                .member(member)
                .postState(PostState.STATE_GENERATING)
                .build();

        List<String> imgUrls = amazonS3Service.uploadFiles(images);
        post.mapPostImageOption(post,request.optionContent(),imgUrls);

        postRepository.save(post);

        return post.getId();
    }

    public Long deletePost(Long postId){

        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NO_VOTE_ID));
        Long deletedPostId = post.getId();

        // 이미지 타입일 경우 s3 서버도 이미지 추가 삭제
        if(post.getPostBody().getPostType()==PostType.IMAGE_OPTION){
            List<ImageOption> imageOptions = post.getImageOptions();
            amazonS3Service.deleteImages(imageOptions);
        }

        postRepository.delete(post);

        return deletedPostId;

    }

    public Long cancelPost(Long postId,Member member){
        // 투표글 취소(삭제)처리
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NO_VOTE_ID));

        postExceptionHandler.cancelPostException(post,member);

        if(post.getPostBody().getPostType()==PostType.IMAGE_OPTION){
            amazonS3Service.deleteImages(post.getImageOptions());
        }
        postRepository.delete(post);

        // 거래내역 취소처리
        Account account = accountRepository.findByTargetMemberAndTargetPost(member, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_ACCOuNT_HISTORY));

        accountExceptionHandler.cancelAccountException(account);

        account.getAccountBody().setAccountStateToCancel();
        accountRepository.save(account);

        // 환불 처리
        member.cancelPoint(account.getAccountBody().getAmount());
        memberRepository.save(member);

        return post.getId();
    }

}
