package com.gollajo.domain.auth.service;

import com.gollajo.domain.auth.dto.KakaoMemberResponse;
import com.gollajo.domain.exception.CustomException;
import com.gollajo.domain.exception.ErrorCode;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;

    public Long register(final String code) {

        final String accessToken = kakaoOAuthClient.getAccessToken(code);
        final KakaoMemberResponse response = kakaoOAuthClient.getMemberInfo(accessToken);
        final String memberEmail = response.kakaoAccount().email();

        boolean isMember = checkIsMember(memberEmail);

        //TODO: 이메일이 DB에 존재하면 로그인 처리
        if(isMember){
            Member alreadyJoinMember = memberService.findByEmail(memberEmail);
            return alreadyJoinMember.getId();
        }
        //TODO: 이메일이 DB에 존재하지 않으면 가입처리
        else{
            final Member member = Member.createKakaoMember(response);
            final Member registeredMember = memberService.register(member);
            return registeredMember.getId();
        }



    }

    private boolean checkIsMember(final String checkingEmail){

        if(checkingEmail==null){
            throw new CustomException(ErrorCode.INTEGRATE_ERROR);
        }

        if(memberService.existsByEmail(checkingEmail)){
            return true;
        }else{
            return false;
        }

    }
}
