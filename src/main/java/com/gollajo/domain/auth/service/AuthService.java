package com.gollajo.domain.auth.service;

import com.gollajo.domain.auth.dto.KakaoMemberResponse;
import com.gollajo.domain.jwt.TokenProcess;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;
    private final TokenProcess tokenProcess;

    public String register(final String code) {

        final String accessToken = kakaoOAuthClient.getAccessToken(code);
        final KakaoMemberResponse response = kakaoOAuthClient.getMemberInfo(accessToken);

        final Member member = Member.createKakaoMember(response);
        final Member registeredMember = memberService.register(member);

        return tokenProcess.generateToken(registeredMember);

    }
}
