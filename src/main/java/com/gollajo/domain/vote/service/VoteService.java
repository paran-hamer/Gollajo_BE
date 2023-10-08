package com.gollajo.domain.vote.service;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.entity.AccountBody;
import com.gollajo.domain.account.entity.enums.AccountState;
import com.gollajo.domain.account.entity.enums.AccountType;
import com.gollajo.domain.account.repository.AccountRepository;
import com.gollajo.domain.exception.CustomException;
import com.gollajo.domain.exception.ErrorCode;
import com.gollajo.domain.exception.handler.VoteExceptionHandler;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.repository.MemberRepository;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.TextOption;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import com.gollajo.domain.post.repository.ImageOptionRepository;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.post.repository.TextOptionRepository;
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
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TextOptionRepository textOptionRepository;
    private final ImageOptionRepository imageOptionRepository;

    private final VoteExceptionHandler voteExceptionHandler;

    public List<VoteResultResponse> createVote(Member member, Long postId, Long optionId) {

        Post post = voteExceptionHandler.voteException(member, postId, optionId);

        Vote vote = makeVote(member, post, optionId);

        voteRepository.save(vote);

        // 투표글 상태 최신화
        int maxVotes = post.getPostBody().getMaxVotes();
        int currentVotes = voteRepository.countByPost(post);
        if(currentVotes >= maxVotes){
            post.setPostState(PostState.STATE_COMPLETE);
            postRepository.save(post);
        }

        //투표한 사람에게 포인트 지급
        member.plusNumOfVoting();
        member.plusPoint(post.getPostBody().getPointPerVote());
        memberRepository.save(member);

        //거래내역 작성
        AccountBody accountBody = AccountBody.builder()
                .amount(post.getPostBody().getPointPerVote())
                .memo("Get voting point")
                .accountState(AccountState.COMPLETE)
                .accountType(AccountType.DEPOSIT)
                .build();
        accountRepository.save(Account.builder()
                .accountBody(accountBody)
                .targetMember(member)
                .targetPost(post)
                .build());

        List<VoteResultResponse> voteResult = getVoteResult(post);
        return voteResult;
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
