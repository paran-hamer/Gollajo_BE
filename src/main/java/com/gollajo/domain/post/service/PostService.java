package com.gollajo.domain.post.service;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.exception.CustomException;
import com.gollajo.domain.exception.ErrorCode;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import com.gollajo.domain.post.dto.*;
import com.gollajo.domain.post.dto.response.*;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.PostBody;
import com.gollajo.domain.post.entity.TextOption;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import com.gollajo.domain.exception.handler.PostExceptionHandler;
import com.gollajo.domain.post.repository.ImageOptionRepository;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.post.repository.TextOptionRepository;
import com.gollajo.domain.s3.AmazonS3Service;
import com.gollajo.domain.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostExceptionHandler postExceptionHandler;

    private final TextOptionRepository textOptionRepository;
    private final ImageOptionRepository imageOptionRepository;

    private final AmazonS3Service amazonS3Service;
    private final AccountService accountService;
    private final MemberService memberService;
    private final VoteService voteService;

    // 진행중인 투표글 리스트 보기
    public List<PostListResponse> showAllPostList(){

        List<Post> postList = postRepository.findAll();

        List<Post> posts = new ArrayList<>();
        for (Post post : postList) {

            if(post.getPostState()==PostState.STATE_PROCEEDING){
                if(checkExpirationAt(post)){
                    continue;
                }
                posts.add(post);
            }
        }
        List<PostListResponse> postListResponses = transPostsToPostListResponses(posts);

        if(postListResponses.isEmpty()){
            throw new CustomException(ErrorCode.NO_VOTE_LIST);
        }
        return postListResponses;

    }

    //객관식형 투표글 생성
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

    //이미지형 투표글 생성
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

    //투표글 삭제
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

    //투표글 취소
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

    //투표글 상세조회
    public PostInfoResponse showPostInfo(Long postId){

        Post post = postExceptionHandler.showPostInfoException(postId);

        List<PostInfoOption> options = makePostInfoOptionList(post);

        PostInfoResponse result = PostInfoResponse.builder()
                .postId(post.getId())
                .title(post.getPostBody().getTitle())
                .content(post.getPostBody().getContent())
                .postType(post.getPostBody().getPostType())
                .postState(post.getPostState())
                .currentPostCount(voteService.CountCurrentPostCount(post))
                .maxPostCount(post.getPostBody().getMaxVotes())
                .pointPerVote(post.getPostBody().getPointPerVote())
                .createdAt(post.getCreatedAt())
                .expirationDate(post.getPostBody().getExpirationDate())
                .options(options)
                .build();

        return result;
    }

    //투표글의 maxVotes가 다 칬을 때 투표글 상태 COMPLETE로 업데이트
    //스프링빈의 순환참조로 인해 voteService에서 이용불가
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

    private List<PostInfoOption> makePostInfoOptionList(Post post){

        List<PostInfoOption> result = new ArrayList<>();

        if(post.getPostBody().getPostType()==PostType.TEXT_OPTION){

            List<TextOption> textOptions = textOptionRepository.findAllByPost(post);

            for(TextOption textOption : textOptions){
                result.add(PostInfoTextOption.builder()
                        .id(textOption.getId())
                        .content(textOption.getStringContent())
                        .build());
            }
        } else{
            List<ImageOption> imageOptions = imageOptionRepository.findAllByPost(post);

            for(ImageOption imageOption : imageOptions){
                result.add(PostInfoImageOption.builder()
                        .id(imageOption.getId())
                        .content(imageOption.getImageContent())
                        .imgUrl(imageOption.getImageUrl())
                        .build());
            }
        }

        return result;
    }

    //만료되었으면 상태변경 후 true, 만료되지 않았으면 false
    private boolean checkExpirationAt(Post post){

        LocalDateTime currentDate = LocalDateTime.now();

        //날짜가 만료되었는지 확인
        if(currentDate.isAfter(post.getPostBody().getExpirationDate())){

            PostState currentState = post.getPostState();

            if(currentState == PostState.STATE_PROCEEDING){

                post.setPostState(PostState.STATE_EXPIRED_AND_NO_COMPLETE);
                postRepository.save(post);

            }else if(currentState == PostState.STATE_COMPLETE){

                post.setPostState(PostState.STATE_EXPIRED_AND_COMPLETE);
                postRepository.save(post);

            }
            return true;
        }
        return false;
    }

    private List<PostListResponse> transPostsToPostListResponses(List<Post> posts){

        List<PostListResponse> result = new ArrayList<>();
        for (Post post : posts) {
            PostListResponse postListResponse = PostListResponse.builder()
                    .postId(post.getId())
                    .postType(post.getPostBody().getPostType())
                    .title(post.getPostBody().getTitle())
                    .content(post.getPostBody().getContent())
                    .pointPerVote(post.getPostBody().getPointPerVote())
                    .expirationDate(post.getPostBody().getExpirationDate())
                    .build();

            result.add(postListResponse);
        }

        return result;
    }

    public void updateProceed(Post post){

        final LocalDateTime currentTime = LocalDateTime.now();
        final LocalDateTime expirationTime = post.getCreatedAt().plusMinutes(5);

        if (currentTime.isAfter(expirationTime) &&
                post.getPostState() == PostState.STATE_GENERATING) {

            log.info("VoteStatus UPDATE: GENERATING -> PROCEEDING");

            post.setPostState(PostState.STATE_PROCEEDING);
            postRepository.save(post);

            //해당하는 거래내역도 최신화
            accountService.updateAccountState(post);

        }

    }

    public void checkExpired(Post post){

        final LocalDateTime currentTime = LocalDateTime.now();
        final LocalDateTime expirationTime = post.getPostBody().getExpirationDate();

        if(currentTime.isAfter(expirationTime)){

            if (post.getPostState() == PostState.STATE_COMPLETE) {

                post.setPostState(PostState.STATE_EXPIRED_AND_COMPLETE);

            }else{

                post.setPostState(PostState.STATE_EXPIRED_AND_NO_COMPLETE);

            }
            postRepository.save(post);

        }

    }

}
