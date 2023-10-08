package com.gollajo.domain.post.exception;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.global.exception.CustomException;
import com.gollajo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostExceptionHandler {

    private final PostRepository postRepository;

    public void cancelPostException(Post post, Member member){
        //작성자 확인 예외
        if(post.getMember()!=member){
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }
        //취소가능한 상태인지 확인 예외
        if(post.getPostState()!=PostState.STATE_GENERATING){
            if(post.getPostState()== PostState.STATE_PROCEEDING){
                throw new CustomException(ErrorCode.CAN_NOT_DELETE_ING_VOTE);
            }else{
                throw new CustomException(ErrorCode.TRY_DELETE_INSTEAD_OF_CANCLE);
            }
        }
    }
}
