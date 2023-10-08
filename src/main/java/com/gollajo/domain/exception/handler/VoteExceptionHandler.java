package com.gollajo.domain.exception.handler;

import com.gollajo.domain.exception.CustomException;
import com.gollajo.domain.exception.ErrorCode;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.TextOption;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import com.gollajo.domain.post.repository.ImageOptionRepository;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.post.repository.TextOptionRepository;
import com.gollajo.domain.vote.entity.Vote;
import com.gollajo.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VoteExceptionHandler {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final ImageOptionRepository imageOptionRepository;
    private final TextOptionRepository textOptionRepository;

    public Post voteException(Member member,Long postId, Long optionId){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_VOTE_ID));

        // postId와 optionId가 제대로 들어온건지 확인 (Text Type)
        if (post.getPostBody().getPostType() == PostType.TEXT_OPTION) {
            //투표글에 해당하는 보기 존재 여부 확인
            TextOption textOption = textOptionRepository.findById(optionId)
                    .orElseThrow(() ->
                            new CustomException(ErrorCode.NO_OPTION_BY_OPTION_ID));

            //선택한 보기가 해당 투표글에 속하는지 확인
            if (!post.getTextOptions().contains(textOption)) {
                throw new CustomException(ErrorCode.WRONG_OPTION_ID_BY_VOTE_ID);
            }

        }
        // postId와 optionId가 제대로 들어온건지 확인 (Image Type)
        if (post.getPostBody().getPostType() == PostType.IMAGE_OPTION) {
            ImageOption imageOption = imageOptionRepository.findById(optionId)
                    .orElseThrow(() ->
                            new CustomException(ErrorCode.NO_OPTION_BY_OPTION_ID));
            if(!post.getImageOptions().contains(imageOption)){
                throw new CustomException(ErrorCode.WRONG_OPTION_ID_BY_VOTE_ID);
            }
        }

        //진행 중인 투표글인지 확인
        if(post.getPostState()!= PostState.STATE_PROCEEDING){
            throw new CustomException(ErrorCode.NO_ING_VOTE);
        }

        //이미 투표한 사용자인지 확인
        if(voteRepository.existsByMemberAndPost(member,post)){
            throw new CustomException(ErrorCode.DUPLICATE_VOTE);
        }

        //작성자와 현재 투표하는 사용자가 일치하는지 확인
        if(post.getMember().equals(member)){
            throw new CustomException(ErrorCode.SELF_VOTE);
        }
        return post;

    }


}
