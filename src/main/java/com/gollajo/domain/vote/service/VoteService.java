package com.gollajo.domain.vote.service;

import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.exception.handler.VoteExceptionHandler;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.TextOption;
import com.gollajo.domain.post.entity.enums.PostType;
import com.gollajo.domain.post.repository.ImageOptionRepository;
import com.gollajo.domain.post.repository.TextOptionRepository;
import com.gollajo.domain.post.service.PostService;
import com.gollajo.domain.vote.dto.VoteResultResponse;
import com.gollajo.domain.vote.entity.Vote;
import com.gollajo.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteExceptionHandler voteExceptionHandler;

    private final TextOptionRepository textOptionRepository;
    private final ImageOptionRepository imageOptionRepository;

    private final AccountService accountService;
    private final PostService postService;
    private final MemberService memberService;



    public List<VoteResultResponse> createVote(Member member, Long postId, Long optionId) {

        Post post = voteExceptionHandler.voteException(member, postId, optionId);

        //투표 생성
        Vote vote = makeVote(member, post, optionId);
        voteRepository.save(vote);

        // 투표글 상태 최신화
        Post updatedPost = postService.updatePostState(post, voteRepository.countByPost(post));

        //투표한 사람에게 포인트 지급
        memberService.saveVotePostMember(member, post.getPostBody().getPointPerVote());

        //거래내역 작성
        accountService.saveVoteAccount(member, updatedPost);

        List<VoteResultResponse> voteResult = getVoteResult(post);
        return voteResult;
    }

    public int CountCurrentPostCount(Post post){
        int currentPostCount = voteRepository.countByPost(post);
        return currentPostCount;
    }

    private Vote makeVote(Member member,Post post,Long optionId){

        Vote vote;
        if (post.getPostBody().getPostType() == PostType.TEXT_OPTION) {
            vote = Vote.builder()
                    .member(member)
                    .post(post)
                    .textOption(textOptionRepository.findById(optionId).get())
                    .build();
        } else{
            vote = Vote.builder()
                    .member(member)
                    .post(post)
                    .imageOption(imageOptionRepository.findById(optionId).get())
                    .build();
        }

        return vote;
    }

    private List<VoteResultResponse> getVoteResult(Post post){

        List<Vote> voteList = voteRepository.findAllByPost(post);

        Map<Long, Integer> voteCounts = new HashMap<>();
        for (Vote vote : voteList) {
            Long optionId = getOptionIdByVote(vote);
            voteCounts.put(optionId,
                    voteCounts.getOrDefault(optionId, 0) + 1);
        }

        List<VoteResultResponse> result = new ArrayList<>();
        for(Map.Entry<Long,Integer> entry: voteCounts.entrySet()){

            Long optionId = entry.getKey();
            int voteCount = entry.getValue();
            String optionContent = getOptionContent(post, optionId);

            result.add(VoteResultResponse.builder()
                    .optionId(optionId)
                    .optionContent(optionContent)
                    .voteCount(voteCount)
                    .postType(post.getPostBody().getPostType())
                    .build());
        }

        return result;
    }

    private Long getOptionIdByVote(Vote vote){

        Long optionId;
        if(vote.getTextOption()!=null){
            optionId = vote.getTextOption().getId();
        }else{
            optionId = vote.getImageOption().getId();
        }

        return optionId;
    }

    private String getOptionContent(Post post,Long optionId){

        String optionContent = "";
        if(post.getPostBody().getPostType()==PostType.TEXT_OPTION){

            TextOption textOption = textOptionRepository.findById(optionId)
                    .orElse(null);

            if (textOption != null) {
                optionContent = textOption.getStringContent();
            } else {
                optionContent = "보기 정보 없음";
            }

        }else{

            ImageOption imageOption = imageOptionRepository.findById(optionId)
                    .orElse(null);

            if (imageOption != null) {
                optionContent = imageOption.getImageContent();
                // optionContent = imageOption.getImageUrl(); //이미지 Url이 필요할때
            }else{
                optionContent = "이미지 보기 정보 없음";
            }
        }

        return optionContent;
    }

}
