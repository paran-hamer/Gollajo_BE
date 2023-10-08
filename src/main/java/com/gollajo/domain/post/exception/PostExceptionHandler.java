package com.gollajo.domain.post.exception;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.dto.PostCreateRequest;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.global.exception.CustomException;
import com.gollajo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class PostExceptionHandler {

    private final PostRepository postRepository;

    private static final int CONTENT_LENGTH=30;         // 제목,내용 길이 제한
    private static final int MINIMuM_SETUP_TIME = 10;   // 최소 설정 시간

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

    public void createPostException(PostCreateRequest request,Member member){

        LocalDateTime validTime = LocalDateTime.now().plusMinutes(MINIMuM_SETUP_TIME);

        //너무 높은 포인트를 설정한지 검증
        if (member.getPoint() < (request.maxVotes()* request.pointPerVote())) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }

        //제목길이가 적당한지 검증
        if(request.title().length() > CONTENT_LENGTH){
            throw new CustomException(ErrorCode.TOO_LONG_TITLE_LENGTH);
        }

        //내용길이가 적당한지 검증
        if(request.content().length() > CONTENT_LENGTH){
            throw new CustomException(ErrorCode.TOO_LONG_CONTENT_LENGTH);
        }

        //마감날짜가 적당한지 검증
        if(validTime.isAfter(request.expirationDate())){
            throw new CustomException(ErrorCode.WRONG_DEADLINE);
        }

    }
}
