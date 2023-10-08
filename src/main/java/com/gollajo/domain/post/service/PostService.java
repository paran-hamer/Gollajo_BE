package com.gollajo.domain.post.service;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import com.gollajo.domain.post.dto.PostCreateRequest;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.PostBody;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import com.gollajo.domain.exception.handler.PostExceptionHandler;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.s3.AmazonS3Service;
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
    private final PostExceptionHandler postExceptionHandler;

    private final AmazonS3Service amazonS3Service;
    private final AccountService accountService;
    private final MemberService memberService;

    public Long createStringPost(PostCreateRequest request, Member member){

        //객관식형 투표글 생성
        Post post = makePost(request, member,PostType.TEXT_OPTION);
        post.mapPostStringOption(post,request.optionContent());
        postRepository.save(post);

        //포인트 지불
        int sumAmount = request.maxVotes() * request.pointPerVote();
        memberService.saveCreatePostMember(member, sumAmount);

        //거래내역 저장
        accountService.saveCreatePostAccount(member, post, "Create textPost");

        //TODO: 반환값을 완성된 투표글 정보를 보여주도록 바꾸기
        return post.getId();

    }

    public Long createImagePost(PostCreateRequest request, Member member, List<MultipartFile> images){

        //이미지형 투표글 생성
        Post post = makePost(request, member,PostType.IMAGE_OPTION);

        List<String> imgUrls = amazonS3Service.uploadFiles(images);

        post.mapPostImageOption(post,request.optionContent(),imgUrls);
        postRepository.save(post);

        //포인트 지불
        int sumAmount = request.maxVotes() * request.pointPerVote();
        memberService.saveCreatePostMember(member, sumAmount);

        //거래내역 저장
        accountService.saveCreatePostAccount(member, post, "Create imagePost");

        //TODO: 반환값을 완성된 투표글 정보를 보여주도록 바꾸기
        return post.getId();
    }

    public Long deletePost(Member member, Long postId) {

        Post post = postExceptionHandler.deletePostException(member, postId);

        // 이미지 타입일 경우 s3 서버도 이미지 추가 삭제
        if (post.getPostBody().getPostType() == PostType.IMAGE_OPTION) {
            List<ImageOption> imageOptions = post.getImageOptions();
            amazonS3Service.deleteImages(imageOptions);
        }

        postRepository.delete(post);

        return post.getId();

    }

    public Long cancelPost(Long postId,Member member){

        // 투표글 취소(삭제)처리
        Post post = postExceptionHandler.cancelPostException(postId, member);

        if(post.getPostBody().getPostType()==PostType.IMAGE_OPTION){
            amazonS3Service.deleteImages(post.getImageOptions());
        }
        postRepository.delete(post);

        // 거래내역 취소처리
        Account account = accountService.saveCancelPostAccount(member, post);

        // 환불 처리
        memberService.saveCancelPostMember(member,
                account.getAccountBody().getAmount());

        return post.getId();
    }

    public Post updatePostState(Post post, int currentVoteCount){

        int maxVoteCount = post.getPostBody().getMaxVotes();

        if(currentVoteCount >=maxVoteCount){
            post.setPostState(PostState.STATE_COMPLETE);
            postRepository.save(post);
        }

        return post;
    }

    private Post makePost(PostCreateRequest request, Member member,PostType postType){

        postExceptionHandler.createPostException(request,member);

        PostBody createdPostBody = PostBody.builder()
                .postType(postType)
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

        return post;
    }

}
