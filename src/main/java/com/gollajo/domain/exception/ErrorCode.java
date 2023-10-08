package com.gollajo.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
    //인증예외(1번대)
    NO_AUTHORITY(HttpStatus.BAD_REQUEST, 100, "권한 없습니다."),
    //유저예외(2번대
    NO_USERNAME(HttpStatus.BAD_REQUEST, 201,"닉네임은 필수입니다."),
    NO_EMAIL(HttpStatus.BAD_REQUEST,202,"이메일은 필수입니다."),
    NO_EMAIL_FROM_KAKAO(HttpStatus.BAD_REQUEST,203,"카카오에서 이메일 불러오기 실패"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST,204,"중복된 이메일입니다."),
    NO_MEMBER_BY_MEMBER_ID(HttpStatus.BAD_REQUEST, 205,"memberId에 해당하는 멤버를 찾을 수 없습니다."),

    //투표글예외(3번대
    NO_VOTE_ID(HttpStatus.NOT_FOUND,301,"voteId에 해당하는 투표글을 찾을 수 없습니다."),
    NO_VOTE_LIST(HttpStatus.BAD_REQUEST,302,"투표리스트가 없습니다"),
    NO_COTENT_BY_ID(HttpStatus.NOT_FOUND,303,"해당 Id로 cotent를 찾을 수 없습니다."),
    NO_OPTION_BY_OPTION_ID(HttpStatus.NOT_FOUND,304,"해당 voteID에 해당하는 option이 없습니다."),
    NO_VOTE_TYPE_ERROR(HttpStatus.BAD_REQUEST, 305,"알 수 없는 투표타입입니다."),
    CAN_NOT_DELETE_ING_VOTE(HttpStatus.BAD_REQUEST, 306,"진행중인 투표글은 삭제할 수 없습니다"),
    TRY_DELETE_INSTEAD_OF_CANCLE(HttpStatus.BAD_REQUEST, 307,"완료된 투표글을 취소대신 삭제를 이용해주세요"),
    TRY_CANCLE_INSTEAD_OF_DELETE(HttpStatus.BAD_REQUEST, 308,"생성중인 투표글은 삭제대신 취소를 이용해주세요"),
    WRONG_OPTION_ID_BY_VOTE_ID(HttpStatus.BAD_REQUEST,309,"선택한 보기가 해당 투표글에 속하지 않습니다."),
    TOO_LONG_TITLE_LENGTH(HttpStatus.BAD_REQUEST, 310,"제목이 너무 깁니다"),
    TOO_LONG_CONTENT_LENGTH(HttpStatus.BAD_REQUEST, 311,"내용이 너무 깁니다"),
    TOO_LONG_OPTION_LENGTH(HttpStatus.BAD_REQUEST,312,"보기옵션의 길이가 너무 깁니다."),
    WRONG_DEADLINE(HttpStatus.BAD_REQUEST, 313,"마감날짜가 너무 빠릅니다. 최소 10분 이상으로 설정해주세요."),
    NO_MATCH_SIZE_IMG_OPTION_AND_IMG_URL(HttpStatus.BAD_REQUEST, 314,"이미지 갯수와 이미지 옵션의 갯수가 다릅니다"),


    //투표 예외(4번대
    DUPLICATE_VOTE(HttpStatus.BAD_REQUEST, 401,"이미 투표를 한 사람입니다."),
    SELF_VOTE(HttpStatus.BAD_REQUEST, 402,"자신에게 투표할 수 없습니다."),
    COMPLETED_VOTE(HttpStatus.BAD_REQUEST, 403,"이미 완료된 투표입니다."),
    BORNING_VOTE(HttpStatus.BAD_REQUEST,404,"생성 중인 투표입니다."),
    NO_ING_VOTE(HttpStatus.BAD_REQUEST, 405,"진행중인 투표가 아닙니다."),
    NO_COMPLETED_VOTE(HttpStatus.BAD_REQUEST,406,"완료된 투표가 아닙니다"),

    //포인트예외(5번대
    NO_WAITING_ACCOUNT_STATUS(HttpStatus.BAD_REQUEST, 501,"취소시간이 끝나서 환불이 불가능합니다"),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, 502,"포인트를 충전하고 다시 시도해주세요"),

    //거래내역예외(6번대
    NO_MATCH_TIME(HttpStatus.BAD_REQUEST,601,"거래내역에 적힌 날짜와 투표생성날짜가 다릅니다."),
    NO_WITHDRAW_HISTORY(HttpStatus.BAD_REQUEST, 602,"포인트가 차감된 기록이 없습니다"),
    NO_ACCOuNT_HISTORY(HttpStatus.BAD_REQUEST,603,"거래내역이 존재하지 않습니다"),
    ;


    ErrorCode(HttpStatus statusCode,int customCode,String message){
        this.statusCode = statusCode;
        this.customCode = customCode;
        this.message = message;
    }

    private final HttpStatus statusCode;
    private final int customCode;
    private final String message;
}
