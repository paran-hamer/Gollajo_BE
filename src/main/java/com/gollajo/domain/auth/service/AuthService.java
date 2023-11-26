package com.gollajo.domain.auth.service;

import com.gollajo.domain.auth.dto.KakaoMemberResponse;
import com.gollajo.domain.exception.CustomException;
import com.gollajo.domain.exception.ErrorCode;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;

    public Long register(final String code) {

        final String accessToken = kakaoOAuthClient.getAccessToken(code);
        final KakaoMemberResponse response = kakaoOAuthClient.getMemberInfo(accessToken);
        final String memberEmail = response.kakaoAccount().email();

        boolean isMember = checkIsMember(memberEmail);

        if(isMember){
            Member alreadyJoinMember = memberService.findByEmail(memberEmail);
            return alreadyJoinMember.getId();
        }
        else{
            Member member = Member.createKakaoMember(response);
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
