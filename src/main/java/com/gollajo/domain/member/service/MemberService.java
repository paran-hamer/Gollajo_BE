package com.gollajo.domain.member.service;

import com.gollajo.domain.member.dto.CreateMemberRequest;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.entity.enums.Grade;
import com.gollajo.domain.member.exception.MemberExceptionHandler;
import com.gollajo.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberExceptionHandler memberExceptionHandler;

    public String signUp(CreateMemberRequest request){

        memberExceptionHandler.signUpException(request);

        Member member = Member.builder()
                .email(request.email())
                .nickname(request.nickname())
                .socialType(request.socialType())
                .socialId(request.socialId())
                .point(0)
                .grade(Grade.LV1)
                .numOfVoting(0)
                .build();

        memberRepository.save(member);

        return "회원가입 성공";
    }

    public Member findById(Long memberId){

        memberExceptionHandler.findByIdException(memberId);

        return memberRepository.findById(memberId).get();
    }


}
